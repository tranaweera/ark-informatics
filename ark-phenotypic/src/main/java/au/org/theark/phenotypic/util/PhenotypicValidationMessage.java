/**
 * 
 */
package au.org.theark.phenotypic.util;

import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;

/**
 * @author cellis
 *
 */
public class PhenotypicValidationMessage
{
	private static StringBuffer stringBuffer = null;
	
	/**
	 * Returns field not of the defined type error message
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataNotDefinedType(Field field, FieldData fieldData){
		stringBuffer = new StringBuffer();
		stringBuffer.append("SubjectUID: ");
		stringBuffer.append(fieldData.getLinkSubjectStudy().getSubjectUID());
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(fieldData.getValue().toString());
		stringBuffer.append(" is not the defined field type: ");
		stringBuffer.append(field.getFieldType().getName());
		return(stringBuffer.toString());
	}
	
	/**
	 * Returns field greater than defined max value error message
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataGreaterThanMaxValue(Field field, FieldData fieldData){
		stringBuffer = new StringBuffer();
		stringBuffer.append("SubjectUID: ");
		stringBuffer.append(fieldData.getLinkSubjectStudy().getSubjectUID());
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(fieldData.getValue().toString());
		stringBuffer.append(" is greater than the defined maximum value: ");
		stringBuffer.append(field.getMaxValue());
		return(stringBuffer.toString());
	}
	
	/**
	 * Returns field less than defined min value error message
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataLessThanMinValue(Field field, FieldData fieldData){
		stringBuffer = new StringBuffer();
		stringBuffer.append("SubjectUID: ");
		stringBuffer.append(fieldData.getLinkSubjectStudy().getSubjectUID());
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(fieldData.getValue().toString());
		stringBuffer.append(" is less than the defined minimum value: ");
		stringBuffer.append(field.getMinValue());
		return(stringBuffer.toString());
	}
	
	/**
	 * Returns field not within the defined encoded values error message
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataNotInEncodedValues(Field field, FieldData fieldData){
		stringBuffer = new StringBuffer();
		stringBuffer.append("SubjectUID: ");
		stringBuffer.append(fieldData.getLinkSubjectStudy().getSubjectUID());
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(fieldData.getValue().toString());
		stringBuffer.append(" is not in the encoded value: ");
		stringBuffer.append(field.getEncodedValues());
		return(stringBuffer.toString());
	}
}
