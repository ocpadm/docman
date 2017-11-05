package ch.agilesolutions.demo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Attribute {

	int order();
	
	int length();

	boolean required();
	
	WidgetType type() default WidgetType.INPUT;

}