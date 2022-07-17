package cn.qingweico.conversion;

import cn.qingweico.bean.BeanInfoExample;

import java.beans.PropertyEditor;

/**
 * @author zqw
 * @date 2021/12/30
 * @see BeanInfoExample
 */
public class PropertyEditorExample {
    public static void main(String[] args) {
        String test = "name = li";

        PropertyEditor propertyEditor = new StringToPropertiesPropertyEditor();

        propertyEditor.setAsText(test);

        System.out.println(propertyEditor.getValue());

        System.out.println(propertyEditor.getAsText());

    }
}
