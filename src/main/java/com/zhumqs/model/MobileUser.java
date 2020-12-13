
package com.zhumqs.model;

import lombok.Data;

import java.util.List;

/**
 * @author mingqizhu
 * @date 20200102
 */
@Data
public class MobileUser {
    private Integer userId;
    private Integer age; // 20-50
    private Integer sex;
    private Integer institute;
    private Integer city;
    private Integer country;
    private List<Integer> interests;
}