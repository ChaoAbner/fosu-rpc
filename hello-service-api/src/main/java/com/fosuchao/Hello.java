package com.fosuchao;

import lombok.*;

import java.io.Serializable;

/**
 * Created by Chao Ye on 2021/2/19
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Hello implements Serializable {

    private String message;

    private String description;
}
