package cn.qingweico.common.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author zhouqingwei
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DictModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public DictModel() {
    }

    public DictModel(String value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 字典value
     */
    private String value;
    /**
     * 字典文本
     */
    private String text;

    public String getTitle() {
        return this.text;
    }

}
