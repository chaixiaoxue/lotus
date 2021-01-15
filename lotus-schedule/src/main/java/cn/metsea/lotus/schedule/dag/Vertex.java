package cn.metsea.lotus.schedule.dag;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Vertex
 */
@Data
@Builder
public class Vertex {

    private Node node;

    private List<Long> upNodeList;

    private List<Long> downNodeList;


}
