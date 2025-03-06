package com.maghrebia.data_extract.Business.Services;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import com.maghrebia.data_extract.DTO.BanqueDTO;

public interface BanqueService {

    public void importBanques(Sheet sheet);
    public List<BanqueDTO> getAllBanques();
    public BanqueDTO updateBanque(Long idBanque, BanqueDTO banqueDTO);
    public void deleteBanqueEntry(Long idTransaction);
    public ResponseEntity<ByteArrayResource> exportBanquesToExcel();
}
