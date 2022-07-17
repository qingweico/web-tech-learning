package cn.qingweico.ioc.domain;

import cn.qingweico.ioc.annotation.Super;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zqw
 * @date 2021/11/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Super
public class SuperUser extends User {
   private String phone;

   @Override
   public String toString() {
      return "SuperUser{" +
              "phone='" + phone + '\'' +
              "} " + super.toString();
   }
}
