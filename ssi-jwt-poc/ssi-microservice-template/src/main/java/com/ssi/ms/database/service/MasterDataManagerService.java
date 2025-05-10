package com.ssi.ms.database.service;

import java.util.Date;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssi.ms.database.AllowValAlvRepository;
import com.ssi.ms.database.NhuisLogNhlRepository;
import com.ssi.ms.database.dao.NhuisLogNhl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MasterDataManagerService {

    @Autowired
    private AllowValAlvRepository allowValAlvRepository;

    @Autowired
     private NhuisLogNhlRepository nhuisLogNhlRepository;

    public void getAllAlvValue(final Long id) {

allowValAlvRepository.findAll()
            .forEach(allowValAlv -> {
                System.out.println(allowValAlv.getAlvId());
            });

        nhuisLogNhlRepository.save(this.createNhuisLog.get());
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private final Supplier<NhuisLogNhl> createNhuisLog = () -> {
        var nhuisLogvar = new NhuisLogNhl();
        nhuisLogvar.setNhlApplnName(1997L);
        nhuisLogvar.setNhlApplnVersion("50.50.23");
        nhuisLogvar.setNhlCreatedBy("6237259");
        nhuisLogvar.setNhlCreatedTs(new Date());
        nhuisLogvar.setNhlCreatedUsing("Spring boot template");
        nhuisLogvar.setNhlErrDesc("Testing with new connection");
        nhuisLogvar.setNhlErrStatusCd(1994L);
        nhuisLogvar.setNhlErrTxt("Testing with new spring boot data conneciton");
        //nhuisLogvar.setNhlId();
        nhuisLogvar.setNhlUpdatedBy("6237259");
        nhuisLogvar.setNhlLastUpdTs(new Date());
        nhuisLogvar.setNhlUpdatedUsing("SpringBootTemplate");
        nhuisLogvar.setNhlLogType(1993L);
        nhuisLogvar.setNhlMethodName("Test method");
        nhuisLogvar.setNhlModuleName("Spring boot microservice");
        nhuisLogvar.setNhlProgName("1990");
      //  nhuisLogvar.setNhlStdErrCd();

        return nhuisLogvar;
    };
}
