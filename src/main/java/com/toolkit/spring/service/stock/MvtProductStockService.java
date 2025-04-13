package com.toolkit.spring.service.stock;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.toolkit.spring.model.table.stock.MvtProductStock;
import com.toolkit.spring.model.view.stock.VLabelMvtProductStock;
import com.toolkit.spring.repository.table.stock.MvtProductStockRepository;
import com.toolkit.spring.repository.view.stock.VLabelMvtProductStockRepository;
import com.toolkit.spring.service.interfaces.OperationService;
import com.toolkit.spring.service.interfaces.ReadService;

@Service
public class MvtProductStockService implements
    OperationService<MvtProductStock, Integer>,
    ReadService<VLabelMvtProductStock, Integer>
{
    @Autowired
    MvtProductStockRepository createRepository;

    @Autowired
    VLabelMvtProductStockRepository readRepository;

    @Override
    public JpaRepository<MvtProductStock, Integer> getOperationRepository()
    { return createRepository; }

    @Override
    public JpaRepository<VLabelMvtProductStock, Integer> getReadRepository()
    { return readRepository; }

    public LocalDate[] findLatestSampleDate(LocalDate dateMax, int sampleCount)
    { return readRepository.findLatestSampleDate(dateMax, sampleCount); }
}
