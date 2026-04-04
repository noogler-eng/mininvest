package com.minivest.mutualfunds.service.impl;
import com.linuxense.javadbf.DBFDataType;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;
import com.minivest.mutualfunds.entity.Transaction;
import com.minivest.mutualfunds.enums.TransactionStatus;
import com.minivest.mutualfunds.repository.TransactionRepo;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Service
public class DbfFileServiceImpl {

    private final TransactionRepo txnRepo;

    public DbfFileServiceImpl(TransactionRepo txnRepo){
        this.txnRepo = txnRepo;
    }


    public byte[] generateOrderDbf(LocalDate date){
        log.info("Generating DBF file for date: {}", date);
        
        // step 1: Define DBF coloumn
        DBFField[] fields = new DBFField[8]; 
        fields[0] = new DBFField();
        fields[0].setName("ORDER_NO");
        fields[0].setType(DBFDataType.CHARACTER);
        fields[0].setLength(20);

        fields[1] = createField("SCHEME_CD", DBFDataType.CHARACTER, 10);
        fields[2] = createField("AMOUNT", DBFDataType.NUMERIC, 15);
        fields[2].setDecimalCount(2);

        fields[3] = createField("INV_NAME", DBFDataType.CHARACTER, 40);
        fields[4] = createField("PAN", DBFDataType.CHARACTER, 10);
        fields[5] = createField("PAY_MODE", DBFDataType.CHARACTER, 2);
        
        fields[6] = createField("TXN_TYPE", DBFDataType.CHARACTER, 1);
        fields[7] = createField("ORDER_DT", DBFDataType.CHARACTER, 10);

        // step 2: Fetch order data for the given date from database
        // Bulk fetch all transactions, then filtering in memory using Java Stream API
        List<Transaction> transactions = txnRepo.findAll().stream()
                .filter(txn -> txn.getStatus() == TransactionStatus.SUCCESS) 
                .filter(txn -> txn.getCreatedAt().toLocalDate().equals(date))
                .toList();

        log.info("Fetched {} transaction for DBF generation", transactions.size());

        // step 3: Write data to DBF file using javadbf library
        // parameter
        // 1. out stream to write the data to.
        // 2. charset Encoding to use in resulting dbf file
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DBFWriter writer = new DBFWriter(baos, StandardCharsets.UTF_8)){
            writer.setFields(fields);

            for (Transaction txn: transactions){
                // making an array of object to represent a row in a DBF file
                // each element in the array corresponds to a column defined in the fields array
                Object[] row = new Object[8];

                row[0] = txn.getRazorpayOrderId();
                row[1] = txn.getSchemeName() != null ? txn.getSchemeName()
                                    .substring(0, Math.min(txn.getSchemeName()
                                        .length(), 10)) : "NA";
                
                row[2] = txn.getAmount().doubleValue();
                row[3] = ""; 
                row[4] = ""; 
                row[5] = "OL"; 
                row[6] = "P";  
                row[7] = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                writer.addRecord(row);
            }
        }

        byte[] dbfFile = baos.toByteArray();
        log.info("DBF file generated successfully with size: {} bytes", dbfFile.length);
        return dbfFile;
    }

    public static DBFField createField(String name, DBFDataType type, int length) {
        DBFField field = new DBFField();
        field.setName(name);
        field.setType(type);
        field.setLength(length);
        return field;
    }
}
