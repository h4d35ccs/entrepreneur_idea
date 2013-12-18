package es.upm.emse.enteridea.adapter.dto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.upm.emse.enteridea.adapter.dto.exception.DTOFactoryException;
import es.upm.emse.enteridea.annotation.dtoannotation.DTOMapping;

/**
 * Generates a DTO from a entity bean
 * 
 * @author ottoabreu
 * 
 * @param <T>
 *            DTO class
 */
public class DTOFactory<T> {

	private Object entity;
	private Class<?> dtoClass;
	// logger
	private static Logger logger = LogManager.getLogger(DTOFactory.class);

	private String GETTER_PREFIX = "get";
	private String GETTER_BOOLEAN_PREFIX = "is";
	private String SETTER_PREFIX = "set";

	/**
	 * Returns an instance of this factory
	 * 
	 * @param entity
	 *            Object entity
	 * @param dtoClass
	 *            Class of the desired DTO
	 * @return {@link DTOFactory}
	 */
	public static DTOFactory<?> getInstance(Object entity, Class<?> dtoClass) {

		DTOFactory<?> factoryInstance = new DTOFactory<Object>(entity, dtoClass);
		return factoryInstance;
	}
	/**
	 * Constructor that not sets the entity, use it when it is necessary to create a list
	 * @param dtoClass
	 * @return
	 */
	public static DTOFactory<?> getInstance(Class<?> dtoClass) {

		DTOFactory<?> factoryInstance = new DTOFactory<Object>(dtoClass);
		return factoryInstance;
	}

	/**
	 * Generate a DTO from the values of an entity<br>
	 * The DTO <B>MUST HAVE THE {@link DTOMapping} annotation </b> otherwise
	 * will not do anything
	 * 
	 * @return T
	 * @throws DTOFactoryException
	 *             if can not instantiate the new DTO or execute the getter or
	 *             setter methods needed
	 */
	@SuppressWarnings("unchecked")
	public T generateDTOFromEntity() throws DTOFactoryException {
		T dto = null;
		try {

			dto = (T) this.dtoClass.newInstance();
			logger.debug("DTO Created " + dto);
			Field[] dtoFields = this.dtoClass.getDeclaredFields();
			logger.debug("fields: " + dtoFields.length);
			this.moveValuesFromEntityToDTO(dtoFields, dto);

		} catch (InstantiationException e) {
			logger.error(DTOFactoryException.INSTANTIATE_DTO_ERROR, e);
			throw new DTOFactoryException(
					DTOFactoryException.INSTANTIATE_DTO_ERROR, e);
		} catch (IllegalAccessException e) {
			logger.error(DTOFactoryException.INSTANTIATE_DTO_ERROR, e);
			throw new DTOFactoryException(
					DTOFactoryException.INSTANTIATE_DTO_ERROR, e);
		} catch (Exception e) {
			logger.error(DTOFactoryException.GENERAL_ERROR, e);
			throw new DTOFactoryException(DTOFactoryException.GENERAL_ERROR, e);
		}

		return dto;
	}
	/**
	 * From a list of entities generate a list of DTO
	 * @param entityList 
	 * @return List
	 * @throws DTOFactoryException if can not create the list
	 */
	public  List<T> generateListDTOFromEntity(List<?> entityList) throws DTOFactoryException {
		List<T> listDto = new ArrayList<T>();
		
		for(Object entity : entityList){
			DTOFactory<?> factoryInstance = new DTOFactory<Object>(
					entity, this.dtoClass);
			@SuppressWarnings("unchecked")
			T dto = (T) factoryInstance.generateDTOFromEntity();
			listDto.add(dto);
		}
		
		return listDto;
	}

	/**
	 * Gets the annotations from the dto field, extract the value from the
	 * entity and set that value to the corresponding field in the dto
	 * 
	 * @param dtoFields
	 *            Field[] that belongs to the DTO
	 * @param dtoInstance
	 *            T actual instance of the DTO
	 * @throws DTOFactoryException
	 *             if an error occurs executing the getter or setter method
	 */
	private void moveValuesFromEntityToDTO(Field[] dtoFields, T dtoInstance)
			throws DTOFactoryException {

		for (Field field : dtoFields) {
			DTOMapping annotation = field.getAnnotation(DTOMapping.class);
			logger.debug("annotation found:" + annotation);
			// the field is a collection of others entities
			if (annotation != null
					&& (!annotation.getValueFrom().equals("") && !annotation
							.generateListOf().equals(Object.class))) {
				logger.debug("mapping list mapping");
				
				// the list of other entities
				Collection<?> obtainedListValueFromEntity = (Collection<?>) this
						.executeGetMethod(annotation.getValueFrom(),
								this.entity);

				// obtain the new class for the other dto inside the list
				Class<?> otherDTOClass = annotation.generateListOf();
				// verify if the obtained entity list have some elements
				if (obtainedListValueFromEntity != null
						&& !obtainedListValueFromEntity.isEmpty()) {

					List<Object> newDTOList = new ArrayList<Object>(
							obtainedListValueFromEntity.size());
					Class<?> listClass = annotation.listSetterClass();
					// for each entity i create a new DTO an put it inside a
					// list
					for (Object otherEntity : obtainedListValueFromEntity) {

						try {
							// execute the same process of 1 on 1 mapping
							DTOFactory<?> factoryInstance = new DTOFactory<Object>(
									otherEntity, otherDTOClass);

							Object newDTOInstance = factoryInstance
									.generateDTOFromEntity();
							newDTOList.add(newDTOInstance);

						} catch (DTOFactoryException e) {
							// one object can fail...we try to continue
							logger.warn(
									"Can't set the element inside the list due an exception, the process will try to add others, error: ",
									e);
						}
						// put the list inside the DTO
						this.executeSetMethod(field.getName(), newDTOList,
								dtoInstance,listClass);
					}
				}

				// is a 1 on 1 mapping
			} else if (annotation != null
					&& (annotation.getSecondValueFrom().equals("") && !annotation
							.getValueFrom().equals(""))) {
				logger.debug("mapping 1 on 1  mapping");
				Object obtainedValueFromEntity = this.executeGetMethod(
						annotation.getValueFrom(), this.entity);
				if (obtainedValueFromEntity != null) {
					logger.debug("Obtained value from getter method: "
							+ obtainedValueFromEntity);
					this.executeSetMethod(field.getName(),
							obtainedValueFromEntity, dtoInstance,null);
				}

				// is necessary to obtain the value from other entity
			} else if (annotation != null
					&& (!annotation.getSecondValueFrom().equals("") && !annotation
							.getValueFrom().equals(""))) {
				logger.debug("mapping other references mapping");
				Object obtainedFirstObjectFromEntity = this.executeGetMethod(
						annotation.getValueFrom(), this.entity);

				if (obtainedFirstObjectFromEntity != null) {

					logger.debug("Obtained first value from getter method: "
							+ obtainedFirstObjectFromEntity);
					Object obtainedValueFromEntity = this.executeGetMethod(
							annotation.getSecondValueFrom(),
							obtainedFirstObjectFromEntity);
					if (obtainedValueFromEntity != null) {

						logger.debug("Obtained second value from getter method: "
								+ obtainedValueFromEntity);
						this.executeSetMethod(field.getName(),
								obtainedValueFromEntity, dtoInstance,null);
					}

				} else {
					logger.warn("The first value is null, can not proceed with the extraction of the second");
				}

				// the annotation is present but there is no information to
				// Retrieve the value
			} else if (annotation != null
					&& (annotation.getSecondValueFrom().equals("") && annotation
							.getValueFrom().equals(""))) {
				logger.warn("Can't map the given field " + field.getName()
						+ " because the mapping annotation is empty");
				// bad configuration
			} else {
				logger.warn("Can't map the given field "
						+ field.getName()
						+ " because the mapping annotation is not configured properly or does not have any annotation (ignore)");
			}

		}
	}

	/**
	 * Executes an getter method from the entity and return the value
	 * 
	 * @param fieldName
	 *            String with the obtained field name from the annotation
	 * @param entity
	 *            Object entity with the values to be included in the DTO
	 * @return Object
	 * @throws DTOFactoryException
	 *             if can't execute the given Get method
	 */
	private Object executeGetMethod(String fieldName, Object entity)
			throws DTOFactoryException {
		// Obtain the method name
		String getterName = this.getSetterGetterMethodName(fieldName,
				GETTER_PREFIX);
		Object obtainedValue = null;

		try {
			// set the class and the value null because is a getmethod, there is
			// no need for setting the param class or value
			logger.debug("method to execute: " + getterName + " Entity: "
					+ entity + " Entity class: " + entity.getClass());
			obtainedValue = this.executeMethod(getterName, null, entity,
					entity.getClass(), null);

		} catch (SecurityException e) {
			logger.error(DTOFactoryException.EXECUTE_GETMETHOD_SECURITY_ERROR,
					e);
			throw new DTOFactoryException(
					DTOFactoryException.EXECUTE_GETMETHOD_SECURITY_ERROR, e);
		} catch (NoSuchMethodException e) {
			logger.debug("trying to execute the getter method using IS prefix ");
			
				try {
					getterName = this.getSetterGetterMethodName(fieldName,
							GETTER_BOOLEAN_PREFIX);
					obtainedValue = this.executeMethod(getterName, null, entity,
							entity.getClass(), null);
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					logger.error(
							DTOFactoryException.EXECUTE_GETMETHOD_ILEGALARGUMENT_ERROR,
							e);
					throw new DTOFactoryException(
							DTOFactoryException.EXECUTE_GETMETHOD_ILEGALARGUMENT_ERROR,
							e);
				} catch (NoSuchMethodException e1) {
					logger.error(DTOFactoryException.EXECUTE_GETMETHOD_NOMETHOD_ERROR,
							e1);
					throw new DTOFactoryException(
							DTOFactoryException.EXECUTE_GETMETHOD_NOMETHOD_ERROR, e1);
				} catch (IllegalAccessException e1) {
					logger.error(
							DTOFactoryException.EXECUTE_GETMETHOD_ILEGALACCES_ERROR, e);
					throw new DTOFactoryException(
							DTOFactoryException.EXECUTE_GETMETHOD_ILEGALACCES_ERROR, e);
				} catch (InvocationTargetException e1) {
					logger.error(DTOFactoryException.EXECUTE_GETMETHOD_IVOTGT_ERROR, e);
					throw new DTOFactoryException(
							DTOFactoryException.EXECUTE_GETMETHOD_IVOTGT_ERROR, e);
				}
			
		} catch (IllegalArgumentException e) {
			logger.error(
					DTOFactoryException.EXECUTE_GETMETHOD_ILEGALARGUMENT_ERROR,
					e);
			throw new DTOFactoryException(
					DTOFactoryException.EXECUTE_GETMETHOD_ILEGALARGUMENT_ERROR,
					e);
		} catch (IllegalAccessException e) {
			logger.error(
					DTOFactoryException.EXECUTE_GETMETHOD_ILEGALACCES_ERROR, e);
			throw new DTOFactoryException(
					DTOFactoryException.EXECUTE_GETMETHOD_ILEGALACCES_ERROR, e);
		} catch (InvocationTargetException e) {
			logger.error(DTOFactoryException.EXECUTE_GETMETHOD_IVOTGT_ERROR, e);
			throw new DTOFactoryException(
					DTOFactoryException.EXECUTE_GETMETHOD_IVOTGT_ERROR, e);
		}
		return obtainedValue;

	}

	/**
	 * Puts the value obtained from the entity into the DTO
	 * 
	 * @param fieldName
	 *            String with the attribute name
	 * @param value
	 *            actual value obtained from the entity
	 * @param dto
	 *            T actual DTO objet
	 *            @param setClass Class<?> used to specify the param type, can be null
	 */
	private void executeSetMethod(String fieldName, Object value, T dto,Class<?> setClass)
			throws DTOFactoryException {
		// Obtain the method name
		String setterName = this.getSetterGetterMethodName(fieldName,
				SETTER_PREFIX);
		// execute the actual setter method
		try {
			logger.debug("method to execute: " + setterName + " Value " + value
					+ " DTO: " + dto);
			Class<?> setterParamClass = null;
			if(setClass != null){
				 setterParamClass = setClass;
			}else{
				 setterParamClass = value.getClass();
			}
			this.executeMethod(setterName,setterParamClass , dto,
					this.dtoClass, value);

		} catch (SecurityException e) {
			logger.error(DTOFactoryException.EXECUTE_SETMETHOD_SECURITY_ERROR,
					e);
			throw new DTOFactoryException(
					DTOFactoryException.EXECUTE_SETMETHOD_SECURITY_ERROR, e);
		} catch (NoSuchMethodException e) {
			logger.error(DTOFactoryException.EXECUTE_SETMETHOD_NOMETHOD_ERROR,
					e);
			throw new DTOFactoryException(
					DTOFactoryException.EXECUTE_SETMETHOD_NOMETHOD_ERROR, e);
		} catch (IllegalArgumentException e) {
			logger.error(
					DTOFactoryException.EXECUTE_SETMETHOD_ILEGALARGUMENT_ERROR,
					e);
			throw new DTOFactoryException(
					DTOFactoryException.EXECUTE_SETMETHOD_ILEGALARGUMENT_ERROR,
					e);
		} catch (IllegalAccessException e) {
			logger.error(
					DTOFactoryException.EXECUTE_SETMETHOD_ILEGALACCES_ERROR, e);
			throw new DTOFactoryException(
					DTOFactoryException.EXECUTE_SETMETHOD_ILEGALACCES_ERROR, e);
		} catch (InvocationTargetException e) {
			logger.error(DTOFactoryException.EXECUTE_SETMETHOD_IVOTGT_ERROR, e);
			throw new DTOFactoryException(
					DTOFactoryException.EXECUTE_SETMETHOD_IVOTGT_ERROR, e);
		}
		// Method setterMethod = this.dtoClass.getMethod(setterName,
		// value.getClass());
		// setterMethod.invoke(dto, value);

	}

	/**
	 * 
	 * @param methodName
	 * @param paramClass
	 * @param invokeFrom
	 * @param invokeFromClass
	 * @param value
	 * @return
	 * @throws SecurityException
	 *             read throw conditions from
	 *             {@link Class#getMethod(java.lang.String, java.lang.Class...)}
	 * @throws NoSuchMethodException
	 *             read throw conditions from
	 *             {@link Class#getMethod(java.lang.String, java.lang.Class...)}
	 * @throws IllegalArgumentException
	 *             read throw conditions from
	 *             {@link Method#invoke(Object, Object...)}
	 * @throws IllegalAccessException
	 *             read throw conditions from
	 *             {@link Method#invoke(Object, Object...)}
	 * @throws InvocationTargetException
	 *             read throw conditions from
	 *             {@link Method#invoke(Object, Object...)}
	 */
	private Object executeMethod(String methodName, Class<?> paramClass,
			Object invokeFrom, Class<?> invokeFromClass, Object value)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		Object toReturn = null;
		Method method = null;

		if (value != null || paramClass != null) {
			method = invokeFromClass.getMethod(methodName, paramClass);
			toReturn = method.invoke(invokeFrom, value);

		} else {
			method = invokeFromClass.getMethod(methodName);
			toReturn = method.invoke(invokeFrom);
		}
		return toReturn;

	}

	/**
	 * Generates the setter name from a field name
	 * 
	 * @param fieldName
	 *            String
	 * @param prefix
	 *            String should be get or set
	 * @return String
	 */
	private String getSetterGetterMethodName(String fieldName, String prefix) {

		StringBuffer setterName = new StringBuffer(prefix);
		// the first letter have to be uppercase
		setterName.append(fieldName.substring(0, 1).toUpperCase());

		// append the rest of the name
		setterName.append(fieldName.substring(1, fieldName.length()));
		logger.debug("method name:" + setterName.toString());
		return setterName.toString();
	}

	/**
	 * private Constructor
	 * 
	 * @param entity
	 * @param Class
	 *            <?> dtoClass
	 */
	private DTOFactory(Object entity, Class<?> dtoClass) {
		super();
		this.entity = entity;
		this.dtoClass = dtoClass;
	}
	
	
	/**
	 * private Constructor
	 * 
	 * @param entity
	 * @param Class
	 *            <?> dtoClass
	 */
	private DTOFactory(Class<?> dtoClass) {
		super();
		
		this.dtoClass = dtoClass;
	}

}
