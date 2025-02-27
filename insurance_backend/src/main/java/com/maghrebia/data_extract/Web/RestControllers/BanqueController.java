package com.maghrebia.data_extract.Web.RestControllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maghrebia.data_extract.Business.ServicesImpl.BanqueServiceImpl;
import com.maghrebia.data_extract.DTO.BanqueDTO;

@RestController
@RequestMapping("/api/view")
public class BanqueController {

    private final BanqueServiceImpl banqueServiceImpl;

    public BanqueController(BanqueServiceImpl banqueServiceImpl){
        this.banqueServiceImpl = banqueServiceImpl;
    }

    @GetMapping("/Banques")
    public ResponseEntity<List<BanqueDTO>> getAllBanques(){
        return ResponseEntity.ok(banqueServiceImpl.getAllBanques());
    }
}
