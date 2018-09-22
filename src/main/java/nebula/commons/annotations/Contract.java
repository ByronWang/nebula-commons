package nebula.commons.annotations;

public @interface Contract {

	boolean pure() default false;

	String value() default "";

}
