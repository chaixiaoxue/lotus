package cn.metsea.lotus.schedule.dag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

/**
 * DAG
 */
@Slf4j
public class DAG {

    private volatile Map<Long, Integer> nodeIndex;

    private volatile List<Vertex> vertexList;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public DAG() {
        nodeIndex = new HashMap<>();
        vertexList = new ArrayList<>();
    }

    /**
     * @param node
     * @return
     */
    public void addVertex(Node node) {
        Long id = node.getId();
        if (this.nodeIsExist(id)) {
            log.info("the Vertex is exist");
            return;
        }
        lock.writeLock().lock();
        try {
            Vertex vertex = Vertex.builder().node(node).downNodeList(new ArrayList<Long>()).upNodeList(new ArrayList<Long>()).build();
            int index = vertexList.size();
            vertexList.add(vertex);
            nodeIndex.put(vertex.getNode().getId(), index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * add edge
     *
     * @return
     */
    public boolean addEdge(Long from, Long to) {

        if (from == null || to == null || !nodeIsExist(from) || !nodeIsExist(to)) {
            log.info("please add node");
            return false;
        }

        if (from.equals(to)) {
            log.info("");
            return false;
        }

    }

    /**
     * whether this node is contained
     *
     * @param id
     * @return
     */
    public boolean nodeIsExist(Long id) {
        lock.readLock().lock();

        try {
            return nodeIndex.containsKey(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isLegalAddEdge(Long from, Long to) {
        if (from.equals(to)) {
            log.info("edge from node {} cant't equals to {} ", from, to);
            return false;
        }
        Vertex toVertex = vertexList.get(nodeIndex.get(to));
        if (CollectionUtils.isEmpty(toVertex.getDownNodeList())) {
            //if to node.downNodeList is empty then add
            return true;
        }
        return false;
    }
}
