package com.gzella.coinMarketSupervisor.presentation.menu;

import com.gzella.coinMarketSupervisor.business.dto.charts.ChartDetailsDTO;
import com.gzella.coinMarketSupervisor.business.service.ChartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/menu/charts")
public class ChartsController {

    ChartDetailsDTO selectedChartDetails;

    @Autowired
    ChartsService chartsService;

    @PutMapping("/set-details")
    public ResponseEntity<ChartDetailsDTO> setChartDetails(@RequestBody ChartDetailsDTO chartDetails) throws Exception {
        selectedChartDetails = chartDetails;
        System.out.println(selectedChartDetails.getCoinSymbol());
        System.out.println(selectedChartDetails.getInterval());
        chartsService.initDataStream(selectedChartDetails);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/display")
    public String displaySelectedChartHTML(Model model) {
        model.addAttribute("details", selectedChartDetails);
        return "menu/charts/display_chart";
    }
}
