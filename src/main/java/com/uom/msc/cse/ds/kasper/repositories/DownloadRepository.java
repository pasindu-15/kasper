package com.uom.msc.cse.ds.kasper.repositories;

import com.uom.msc.cse.ds.kasper.entities.Downloader;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface DownloadRepository extends   CrudRepository<Downloader, Long>{

        List<Downloader> findByIpAddress(String ipAddress);
}
