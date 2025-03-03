package com.maghrebia.data_extract.Business.Services;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import com.maghrebia.data_extract.DTO.BanqueDTO;

public interface BanqueService {

    public void importBanques(Sheet sheet);
    public List<BanqueDTO> getAllBanques();
    public void deleteBanqueEntry(Long idTransaction);
}
