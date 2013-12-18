package es.upm.emse.enteridea.annotation.dtoannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.List;



/**
 * Annotation that permit make the map between a DTO and an entity<BR>
 * Examples of usage are in the methods<br>
 * <B>For primitives attributes, use Objects as parameters in the setters methods otherwise an exception will rise</B>
 * @author ottoabreu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DTOMapping {
	
	
	/**
	 * Permit to know from what attribute in the entity will be 
	 * extracted the value to be set in the DTO attribute.<BR>
	 * Use attributes name only example:<BR>
	 * Class FooEntity{<BR>
	 * <b>private int myAttribute;</b><BR>
	 * public int getMyAttribute(){}<BR>
	 * public void setMyAttribute(int x){}<BR>
	 * }<BR>
	 * <BR>
	 * <BR>
	 * Class FooDTO{<BR>
	 * <b>DTOMapping(getValueFrom = "myAttribute")</b>
	 * private int myAttributeDTO;<BR>
	 * public int getMyAttribute(){}<BR>
	 * public void setMyAttribute(int x){}<BR>
	 * }
	 * 
	 * @return String
	 */
	String getValueFrom() default "";
	/**
	 * Used when a collection must be generated,<br>
	 * Tells what kind of object must be instantiated inside the collection
	 * Use attributes name only example:<BR>
	 * Class FooEntity{<BR>
	 * <b>private List myAttributeList;</b>// List of instances MyOtherClass.java<BR>
	 * public list getMyAttributeList(){}<BR>
	 * public void setMyAttributeList(List x){}<BR>
	 * }<BR>
	 * <BR>
	 * <BR>
	 * Class FooDTO{<BR>
	 * <b>DTOMapping(getValueFrom = "myAttributeList",generateListOf = MyOtherClass.class )</b>
	 * private List myAttributeDTO;<BR>
	 * public int getMyAttribute(){}<BR>
	 * public void setMyAttribute(int x){}<BR>
	 * }
	 * 
	 * @return Class
	 */
	Class<?> generateListOf() default Object.class;
	/**
	 * When is necessary to obtain a value from another entity
	 * Use attributes name only example:<BR>
	 * Class FooEntity{<BR>
	 * <b>private MyOtherClass myAttribute;</b>//  MyOtherClass.java have a String myOtherAttribute<BR>
	 * public MyOtherClass getMyAttribute(){}<BR>
	 * public void setMyAttribute(MyOtherClass x){}<BR>
	 * }<BR>
	 * <BR>
	 * <BR>
	 * Class FooDTO{<BR>
	 * <b>DTOMapping(getValueFrom = "myAttribute",getSecondValueFrom = "myOtherAttribute" )</b>
	 * private String myOtherAttribute;<BR>
	 * public String getMyOtherAttribute(){}<BR>
	 * public void setMyOtherAttribute(String x){}<BR>
	 * }
	 * @return String
	 */
	String getSecondValueFrom() default "";
	/**
	 * Used to specify the type of the collection param in the DTO setter method<br>
	 * Default {@link java.util.List} 
	 * @return Class<?>
	 */
	Class<?> listSetterClass() default List.class;
}
