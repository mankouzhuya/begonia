package com.github.begonia.core.context;

import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.util.List;

@ToString
@Data
public class MethodNode implements Serializable {

    private String methodId;

    private String methodFullName;

    private String parentMethodId;

    private List<MethodNode> methodNodes;


}
