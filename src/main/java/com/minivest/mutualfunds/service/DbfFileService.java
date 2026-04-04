package com.minivest.mutualfunds.service;

import java.time.LocalDate;

public interface DbfFileService {
    public byte[] generateOrderDbf(LocalDate date);
}
