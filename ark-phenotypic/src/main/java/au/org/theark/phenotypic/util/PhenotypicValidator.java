package au.org.theark.phenotypic.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;

/**
 * PhenotypicValidator provides support for validating phenotypic data with the defined data dictionary
 * 
 * @author cellis
 */
@SuppressWarnings("unused")
public class PhenotypicValidator
{
	static Logger	log	= LoggerFactory.getLogger(PhenotypicValidator.class);
	boolean qualityControl = false;

	/**
	 * PhenotypicValidator constructor
	 * 
	 */
	public PhenotypicValidator()
	{
	}

	public boolean isQualityControl() {
		return qualityControl;
	}

	public void setQualityControl(boolean qualityControl) {
		this.qualityControl = qualityControl;
	}

	/**
	 * Returns true of the field data value is a valid format, either NUMBER, CHARACTER or DATE as specified in the data dictionary
	 * 
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isValidFieldData(FieldData fieldData, java.util.Collection<String> errorMessages)
	{
		Field field = (Field) fieldData.getField();
		// errorMessages.add("Validating field data: " + field.getName().toString() + "\t" + fieldData.getValue().toString());

		// Number field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER))
		{
			try
			{
				Float floatFieldValue = Float.parseFloat(fieldData.getValue());
				return true;
			}
			catch (NumberFormatException nfe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDataNotDefinedType(field, fieldData));
				log.error("Field data number format exception " + nfe.getMessage());
				return false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				return false;
			}
		}

		// Character field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER))
		{
			try
			{
				String stringFieldValue = fieldData.getValue();
				return true;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				return false;
			}
		}

		// Date field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))
		{
			try
			{
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(fieldData.getValue());
				return true;
			}
			catch (ParseException pe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDataNotValidDate(field, fieldData));
				log.error("Field data date parse exception " + pe.getMessage());
				return false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				return false;
			}

		}

		return false;
	}

	/**
	 * Returns true if field data value is within the defined range as specified in the data dictionary
	 * 
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isInValidRange(FieldData fieldData, java.util.Collection<String> errorMessages)
	{
		Field field = (Field) fieldData.getField();

		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER))
		{
			try
			{
				Float floatMinValue = Float.parseFloat(field.getMinValue());
				Float floatMaxValue = Float.parseFloat(field.getMaxValue());
				Float floatFieldValue = Float.parseFloat(fieldData.getValue());

				if ((floatFieldValue > floatMaxValue) || (floatFieldValue < floatMinValue))
				{
					if ((floatFieldValue > floatMaxValue))
					{
						errorMessages.add(PhenotypicValidationMessage.fieldDataGreaterThanMaxValue(field, fieldData));
					}
					if ((floatFieldValue < floatMinValue))
					{
						errorMessages.add(PhenotypicValidationMessage.fieldDataLessThanMinValue(field, fieldData));
					}
					return false;
				}
				return true;
			}
			catch (NumberFormatException nfe)
			{
				log.error("Field data number format exception " + nfe.getMessage());
				return false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				return false;
			}

		}
		return false;
	}

	/**
	 * Returns true if the field data value is within the discrete range as defined in the data dictionary
	 * 
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isInEncodedValues(FieldData fieldData, java.util.Collection<String> errorMessages)
	{
		boolean inEncodedValues = true;

		Field field = (Field) fieldData.getField();

		// Validate if encoded values is defined
		if (field.getEncodedValues() != null)
		{
			try
			{
				StringTokenizer stringTokenizer = new StringTokenizer(field.getEncodedValues(), Constants.ENCODED_VALUES_TOKEN);

				// Iterate through all discrete defined values and compare to field data value
				while (stringTokenizer.hasMoreTokens())
				{
					String encodedValueToken = stringTokenizer.nextToken();
					StringTokenizer encodedValueSeparator = new StringTokenizer(encodedValueToken, Constants.ENCODED_VALUES_SEPARATOR);
					String encodedValue = encodedValueSeparator.nextToken().trim();
					
					if (encodedValue.equalsIgnoreCase(fieldData.getValue().trim()))
					{
						inEncodedValues = true;
						break;
					}
					else
					{
						inEncodedValues = false;
					}
				}

				if (!inEncodedValues)
				{
					errorMessages.add(PhenotypicValidationMessage.fieldDataNotInEncodedValues(field, fieldData));
				}

			}
			catch (NullPointerException npe)
			{
				log.error("Field data null format exception " + npe.getMessage());
				inEncodedValues = false;
			}

		}
		return inEncodedValues;
	}

	public static void validateFieldData(FieldData fieldData, java.util.Collection<String> errorMessages)
	{
		// Validate the field data
		if (isValidFieldData(fieldData, errorMessages))
		{
			// log.info("Field data valid");
		}

		if (isInEncodedValues(fieldData, errorMessages))
		{
			// log.info("Field data in discrete values");
		}

		// Validate the field data within discrete values
		if (isInValidRange(fieldData, errorMessages))
		{
			// log.info("Field data in valid range");
		}
	}
	
	public static boolean fieldDataPassesQualityControl(FieldData fieldData, java.util.Collection<String> errorMessages)
	{
		boolean passesQualityControl = true;
		// Validate the field data
		if (isValidFieldData(fieldData, errorMessages) && isInEncodedValues(fieldData, errorMessages) && isInValidRange(fieldData, errorMessages))
		{
			passesQualityControl = true;
		}
		else
		{
			passesQualityControl = false;
		}
		return passesQualityControl;
	}
}