package unit;

import java.io.Serializable;

public interface SerializableObj extends Serializable{
	
	/**
	 * Those implemented SerializableObj class must exist DEFAULT SIMPLE CONSTRUCTOR
	 * for creating instance in Neuron at server side.
	 */
	
	/**
	 * the rules how implemented domain object be deserialized.
	 * @param input
	 */
	public void deserializeFrom(DataInput input) throws Exception;
	
	/**
	 * the rules how implemented domain object be serialized.
	 * @param output
	 */
	public void serializeTo(DataOutput output) throws Exception;
	
}
