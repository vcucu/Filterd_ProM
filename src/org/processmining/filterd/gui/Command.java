package org.processmining.filterd.gui;

//command pattern implemented

/**
 * 
 * Interface to be implemented by undo-able and redo-able commands 
 * that will change the state of the notebook whose reference 
 * should be passed through the constructor of the command 
 *
 */
public interface Command {
	//reference to notebook object that the command modifies 
	//reference to state (information necessary to be saved for 
	//the undo-ing action of the command)
	//private boolean variable denoting if the command has been executed or no
	
	/**
	 * performs action that changes the state
	 * stores additional state information to be used by undo() operation
	 */
	public void execute();
	
	/**
	 * returns true if the command has been executed, false if it hasn't
	 * @return
	 */
	public boolean isExecuted();
	
	/**
	 * undoes the effect of the last call of the execute() operation
	 */
	
	public void undo();
	
	/**
	 * makes a shallow copy of the notebook reference and deep copy to the state 
	 * @return copy of this command to be placed on the UndoRedo stack
	 */
	public Command copy();
	
}
