package com.drps.ams.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.PaymentOrVoucharCancelDTO;
import com.drps.ams.dto.PaymentSaveDTO;
import com.drps.ams.service.PaymentService;
import com.drps.ams.util.Utils;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	private static final  Logger logger = LogManager.getLogger(PaymentController.class);
	
	@Autowired
	PaymentService paymentService;
	
	@GetMapping(value = "/add_page/get/{id}")
	public ResponseEntity<ApiResponseEntity> getAddPageDetails(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - PaymentController getAddPageDetails");		
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.getAddPageDetails(id));		
	}
	
	@GetMapping(value = "/list_view/get")
	public ResponseEntity<ApiResponseEntity> getListView(@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - PaymentController getListView");		
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.getListView(reqParams));		
	}
	
	@PostMapping(value = "/save")
	public ResponseEntity<ApiResponseEntity> save(@RequestBody PaymentSaveDTO maintenanceSaveDTO) throws Exception {
		logger.info("AMS - PaymentController save");		
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.save(maintenanceSaveDTO));		
	}
	
	@GetMapping(value = "/get/{id}")
	public ResponseEntity<ApiResponseEntity> get(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - PaymentController get");		
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.get(id));		
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<ApiResponseEntity> deleteById(@PathVariable("id") Long id) throws Exception {
		logger.info("AMS - PaymentController deleteById");
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.deleteById(id));		
	}
	
	@PatchMapping(value = "/cancel")
	public ResponseEntity<ApiResponseEntity> cancel(@NonNull @RequestBody PaymentOrVoucharCancelDTO paymentCancel) throws Exception {
		logger.info("AMS - PaymentController cancel");
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.cancel(paymentCancel));		
	}
	
	@GetMapping(value = "/download/{id}")
	public void download(HttpServletRequest request, HttpServletResponse response, 
				@PathVariable("id") Long id,
				@RequestParam(name="params", required = false) String reqParams) throws Exception {
		logger.info("AMS - PaymentController download");
		
//		File file = new File("C:/Users/002ZX2744/Desktop/TEMP/pdf/FirstPdf.pdf");
		File file = paymentService.getFileToDownload(id);
		Utils.downloadPdfFile(request, response, file);
	}
	
	@GetMapping(value = "/slip_by_months/get")
	public ResponseEntity<ApiResponseEntity> getSlipByMonths (){
		logger.info("AMS - PaymentController getSlipByMonths");		
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.getSlipByMonths());
	}
	
	@GetMapping(value = "/download_zip/{fn}")
	public void downloadZip(@PathVariable("fn") String folderName,
			HttpServletRequest request, HttpServletResponse response
			) throws Exception {
		logger.info("AMS - PaymentController downloadZip"+folderName);	
		File file = paymentService.downloadZip(folderName);
		Utils.downloadPdfFile(request, response, file);
	}
	
}
