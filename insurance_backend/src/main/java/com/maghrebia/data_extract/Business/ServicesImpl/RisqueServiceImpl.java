package com.maghrebia.data_extract.Business.ServicesImpl;

import java.util.Iterator;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.maghrebia.data_extract.Business.Services.RisqueService;
import com.maghrebia.data_extract.DAO.Entities.Risque;
import com.maghrebia.data_extract.DAO.Repositories.RisqueRepository;
import com.maghrebia.data_extract.DTO.RisqueDTO;
import com.maghrebia.data_extract.Mapper.RisqueMapper;
import com.maghrebia.data_extract.Utils.ExcelCellUtil;
import com.maghrebia.data_extract.Utils.ExcelRowUtil;

@Service
public class RisqueServiceImpl implements RisqueService {

    private final RisqueRepository risqueRepository;
    private final RisqueMapper risqueMapper;
    private final Logger logger = LoggerFactory.getLogger(RisqueServiceImpl.class);

    public RisqueServiceImpl(RisqueRepository risqueRepository,
            RisqueMapper risqueMapper) {
        this.risqueRepository = risqueRepository;
        this.risqueMapper = risqueMapper;
    }

    @Override
    public void importRisques(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Integer codeRisque = ExcelCellUtil.getCellValue(row, 0, Integer.class);
            String risqueName = ExcelCellUtil.getCellValue(row, 1, String.class);
            Float commission = ExcelCellUtil.getCellValue(row, 3, Float.class);

            if (ExcelRowUtil.isRowEmpty(codeRisque, risqueName, commission)) {
                continue;
            }

            if(codeRisque == null){
                continue;
            } else {

                RisqueDTO risqueDTO = new RisqueDTO(codeRisque, risqueName, commission);
                Risque risque = risqueMapper.toEntity(risqueDTO);

                risqueRepository.save(risque);
            }
        }
    }

    public Optional<Risque> findBycodeRisque(Integer codeRisque) {
        return risqueRepository.findBycodeRisque(codeRisque);
    }

}
