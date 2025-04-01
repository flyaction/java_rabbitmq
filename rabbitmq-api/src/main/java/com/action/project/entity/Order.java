package com.action.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: action
 * @create: 2025/3/31 16:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    public String id;
    public String name;
    public String content;
}
