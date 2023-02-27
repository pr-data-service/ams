package com.drps.ams.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.drps.ams.dto.PaymentCancelDTO;
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
	public ResponseEntity<ApiResponseEntity> cancel(@NonNull @RequestBody PaymentCancelDTO paymentCancel) throws Exception {
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
	
	@GetMapping(value = "/advance_payment_dues_list/get/{flat_id}/{month}/{year}")
	public ResponseEntity<ApiResponseEntity> getDuesListForAdvancePayment(
			@PathVariable("flat_id") Long flatId,
			@PathVariable("month") Integer month,
			@PathVariable("year") Integer year) throws Exception {
		logger.info("AMS - PaymentController getDuesListForAdvancePayment");
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.getDuesListForAdvancePayment(flatId, month, year));		
	}
}
