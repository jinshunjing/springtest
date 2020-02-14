package org.jsj.plugin.mybatis;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.*;
import java.util.Map.Entry;

/**
 * A MyBatis Generator plugin to use Lombok's annotations.
 * For example, use @Data annotation instead of getter ands setter.
 *
 * @author Paolo Predonzani (http://softwareloop.com/)
 */
public class LombokPlugin extends PluginAdapter {

    private final Collection<Annotations> annotations;

    /**
     * LombokPlugin constructor
     */
    public LombokPlugin() {
        annotations = new HashSet<Annotations>(Annotations.values().length);
    }

    /**
     * @param warnings
     * @return always true
     */
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * Intercepts base record class generation
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass, true);
        addTableColumn(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * Intercepts primary key class generation
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass, false);
        addTableColumn(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * Intercepts "record with blob" class generation
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass, true);
        addTableColumn(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * Prevents all getters from being generated.
     * See SimpleModelGenerator
     *
     * @param method
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    @Override
    public boolean modelGetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    /**
     * Prevents all setters from being generated
     * See SimpleModelGenerator
     *
     * @param method
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    @Override
    public boolean modelSetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    /**
     * Adds the lombok annotations' imports and annotations to the class
     *
     * @param topLevelClass
     */
    private void addDataAnnotation(TopLevelClass topLevelClass, boolean builder) {
        for (Annotations annotation : annotations) {
            if (!builder && annotation.paramName.equals("builder")) {
                continue;
            }
            topLevelClass.addImportedType(annotation.javaType);
            topLevelClass.addAnnotation(annotation.name);
        }

    }

    private void addTableColumn(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        InnerEnum innerEnum = new InnerEnum(new FullyQualifiedJavaType("COLUMNS"));
        innerEnum.setVisibility(JavaVisibility.PUBLIC);
        innerEnum.setStatic(true);

        Field columnField = new Field("column", FullyQualifiedJavaType.getStringInstance());
        columnField.setVisibility(JavaVisibility.PRIVATE);
        columnField.addAnnotation(Annotations.GETTER.name);
        innerEnum.addField(columnField);

        introspectedTable.getAllColumns().forEach(introspectedColumn -> {
            StringBuffer sb = new StringBuffer();
            sb.append(introspectedColumn.getActualColumnName().toUpperCase());
            sb.append("(\"");
            sb.append(introspectedColumn.getActualColumnName());
            sb.append("\")");
            innerEnum.addEnumConstant(sb.toString());
        });
        innerEnum.addAnnotation(Annotations.ALL_ARGS_CONSTRUCTOR.name);

        topLevelClass.addInnerEnum(innerEnum);
    }


    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);

        //@Data is default annotation
        //annotations.add(Annotations.DATA);
        annotations.add(Annotations.GETTER);
        annotations.add(Annotations.SETTER);
        annotations.add(Annotations.TO_STRING);
        annotations.add(Annotations.BUILDER);
        annotations.add(Annotations.ALL_ARGS_CONSTRUCTOR);
        annotations.add(Annotations.NO_ARGS_CONSTRUCTOR);

        for (Entry<Object, Object> entry : properties.entrySet()) {
            boolean isEnable = Boolean.parseBoolean(entry.getValue().toString());

            if (isEnable) {
                String paramName = entry.getKey().toString().trim();
                Annotations annotation = Annotations.getValueOf(paramName);
                if (annotation != null) {
                    annotations.add(annotation);
                    annotations.addAll(Annotations.getDependencies(annotation));
                }
            }
        }
    }

    private enum Annotations {
        GETTER("getter", "@Getter", "lombok.Getter"),
        SETTER("setter", "@Setter", "lombok.Setter"),
        DATA("data", "@Data", "lombok.Data"),
        BUILDER("builder", "@Builder", "lombok.Builder"),
        ALL_ARGS_CONSTRUCTOR("allArgsConstructor", "@AllArgsConstructor", "lombok.AllArgsConstructor"),
        NO_ARGS_CONSTRUCTOR("noArgsConstructor", "@NoArgsConstructor", "lombok.NoArgsConstructor"),
        TO_STRING("toString", "@ToString", "lombok.ToString");

        private final String paramName;
        private final String name;
        private final FullyQualifiedJavaType javaType;

        Annotations(String paramName, String name, String className) {
            this.paramName = paramName;
            this.name = name;
            this.javaType = new FullyQualifiedJavaType(className);
        }

        private static Annotations getValueOf(String paramName) {
            for (Annotations annotation : Annotations.values()){
                if (String.CASE_INSENSITIVE_ORDER.compare(paramName, annotation.paramName) == 0) {
                    return annotation;
                }
            }

            return null;
        }

        private static Collection<Annotations> getDependencies(Annotations annotation) {
            if (annotation == ALL_ARGS_CONSTRUCTOR) {
                return Collections.singleton(NO_ARGS_CONSTRUCTOR);
            } else {
                return Collections.emptyList();
            }
        }
    }
}
