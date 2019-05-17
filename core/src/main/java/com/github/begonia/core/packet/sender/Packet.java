package com.github.begonia.core.packet.sender;

import com.github.begonia.core.context.MethodNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Packet implements Serializable {

    private String trackId;

    private List<MethodNode> methodNodes;

}
