package com.minivest.mutualfunds.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minivest.mutualfunds.service.DbfFileService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    DbfFileService dbfFileService;

    public AdminController(DbfFileService dbfFileService){
        this.dbfFileService = dbfFileService;
    }

    @GetMapping("/generate-dbf")
    public ResponseEntity<byte[]> downloadDbf(@RequestParam String date) throws IOException {
        LocalDate d = LocalDate.parse(date);
        
        byte[] dfbFile = dbfFileService.generateOrderDbf(d);
        return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=orders_" + date + ".dbf")
        .header("Content-Type", "application/octet-stream")
        .body(dfbFile);
    }

    
}
