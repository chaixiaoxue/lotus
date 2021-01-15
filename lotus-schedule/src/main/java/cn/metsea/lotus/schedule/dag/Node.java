package cn.metsea.lotus.schedule.dag;

import lombok.Data;

/**
 * @author chaixiaoxue
 * @version 1.0
 * @date 2021/1/14 11:14
 */
@Data
class Node {

    private Long Id;

    public Node(Long Id) {
        this.Id = Id;
    }
}
