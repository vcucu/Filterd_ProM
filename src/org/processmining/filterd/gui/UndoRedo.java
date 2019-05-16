package org.processmining.filterd.gui;

import java.util.Stack;

// implements singleton design pattern/command pattern
// maybe we want to be able to return commands or to undo or redo all commands
//clearing of redo post did()

public class UndoRedo {

	private static UndoRedo instance;
	private static Stack<Command> undoStack;
	private static Stack<Command> redoStack;

	private UndoRedo() {
		undoStack = new Stack<Command>();
		redoStack = new Stack<Command>();

	}

	//Get the only object available
	public static UndoRedo getInstance() {
		if (instance == null)
			instance = new UndoRedo();
		return instance;
	}

	/**
	 * Returns whether an {@code undo} is possible.
	 * 
	 * @return whether {@code undo} is possible
	 */
	private boolean canUndo() {
		return ((!undoStack.isEmpty()) ? true : false);
	}

	/**
	 * Returns whether a {@code redo} is possible.
	 * 
	 * @return {@code redo().pre}
	 */
	private boolean canRedo() {
		return ((!redoStack.isEmpty()) ? true : false);
	}

	/**
	 * Undoes most recently done command
	 */
	public void undo() {
		if (!canUndo()) {
			throw new IllegalStateException("No command was done");
		} else {
			Command mostRecentCommand = undoStack.pop();
			mostRecentCommand.undo();
			redoStack.push(mostRecentCommand);
		}
	}

	/**
	 * Redoes most recently undone command
	 */
	public void redo() {
		if (!canRedo()) {
			throw new IllegalStateException("No command was undone");
		} else {
			Command mostRecentCommand = redoStack.pop();
			mostRecentCommand.execute();
			undoStack.push(mostRecentCommand);
		}
	}

	/**
	 * Clears all undo-redo history.
	 */
	public void clear() {
		undoStack.clear();
		redoStack.clear();
	}

	/**
	 * Adds given command to the undoStack. If the command was not yet
	 * executed, it is first executed.
	 *
	 * @param command
	 *            the command to incorporate
	 */
	public void did(final Command command) {
		if (command.isExecuted()) {
			undoStack.push(command);
		} else {
			command.execute();
			undoStack.push(command);
		}
		//redoStack.clear();
	}

}