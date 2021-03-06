package cn.metsea.lotus.server.master;

import cn.metsea.lotus.server.master.registry.MasterRegistry;
import cn.metsea.lotus.server.master.remote.MasterRemote;
import cn.metsea.lotus.server.master.tolerant.MasterTolerant;
import cn.metsea.lotus.server.worker.WorkerServer;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * Master Server
 */
@Slf4j
@ComponentScan(
    basePackages = "cn.metsea.lotus",
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WorkerServer.class})
    }
)
public class MasterServer {

    @Autowired
    private MasterRemote masterRemote;

    @Autowired
    private MasterRegistry masterRegistry;

    @Autowired
    private MasterTolerant masterTolerant;

    public static void main(String[] args) {
        Thread.currentThread().setName("MasterServer");
        new SpringApplicationBuilder(MasterServer.class).web(WebApplicationType.NONE).run(args);
    }

    @PostConstruct
    public void run() throws InterruptedException {
        // remote
        this.masterRemote.start();

        // registry
        this.masterRegistry.registry();

        // tolerant
        this.masterTolerant.start();

        Thread.sleep(1000 * 600);
    }

}
