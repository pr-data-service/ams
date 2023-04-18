package com.drps.ams.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dbquery.DBQueryExecuter;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EventsDTO;
import com.drps.ams.dto.MiscellaneousFieldDTO;
import com.drps.ams.dto.PaymentDTO;
import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.dto.PaymentOrVoucharCancelDTO;
import com.drps.ams.dto.PaymentSaveDTO;
import com.drps.ams.dto.PaymentSlipByMonthsDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
import com.drps.ams.entity.PaymentEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.exception.FileStorageException;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.repository.EventsRepository;
import com.drps.ams.repository.FlatDetailsRepository;
import com.drps.ams.repository.LinkFlatDetailsAndUserDetailsRepository;
import com.drps.ams.repository.MaintenanceMasterRepository;
import com.drps.ams.repository.MaintenanceRepository;
import com.drps.ams.repository.NotesRepository;
import com.drps.ams.repository.PaymentDetailsRepository;
import com.drps.ams.repository.PaymentRepository;
import com.drps.ams.repository.SessionDetailsRepository;
import com.drps.ams.repository.UserDetailsRepository;
import com.drps.ams.service.BillNoService;
import com.drps.ams.service.CommonService;
import com.drps.ams.service.FlatDetailsService;
import com.drps.ams.service.NotesService;
import com.drps.ams.service.PaymentDetailsService;
import com.drps.ams.service.PaymentService;
import com.drps.ams.service.SessionDetailsService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DateUtils;
import com.drps.ams.util.ExcelFileUtils;
import com.drps.ams.util.FileUtils;
import com.drps.ams.util.Utils;
import com.drps.ams.util.ZipFileUtils;
import com.drps.pdf.PaymentReceiptPDF;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

	@Value("${payment-receipt.storage.path}")
	String FILE;
	
	@Autowired
	FlatDetailsService flatDetailsService;
	
	@Autowired
	LinkFlatDetailsAndUserDetailsRepository linkFlatDetailsAndUserDetailsRepository;
	
	@Autowired
	MaintenanceRepository maintenanceRepository;
	
	@Autowired
	PaymentRepository paymentRepository;
	
	@Autowired
	FlatDetailsRepository flatDetailsRepository;
	
	@Autowired
	MaintenanceMasterRepository maintenanceMasterRepository;
	
	@Autowired
	EventsRepository eventsRepository;
	
	@Autowired
	PaymentDetailsRepository paymentDetailsRepository;
	
	@Autowired
	SessionDetailsRepository sessionDetailsRepository;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	SessionDetailsService sessionDetailsService;
	
	@Autowired
	PaymentDetailsService paymentDetailsService;
	
	@Autowired
	DBQueryExecuter dbQueryExecuter;
	
	@Autowired
	BillNoService billNoService;
	
	@Autowired
	NotesRepository notesRepository;
	
	@Autowired
	NotesService notesService;
	
	@Autowired
	UserDetailsRepository userDetailsRepository;
	
	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		UserContext userContext = Utils.getUserContext();
			
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		
		List<PaymentDTO> rtnList = dbQueryExecuter.executeQuery(new QueryMaker(reqParamDto, PaymentDTO.class));
		//commonService.addPaymentByToDTO(rtnList);
		commonService.addUserDetailsToDTO(rtnList, PaymentDTO.class);
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public ApiResponseEntity getAddPageDetails(Long id) {
		
		UserContext userContext = Utils.getUserContext();
		
		
		Map<String, Object> respMap = new HashMap<>();
		if(id != null && id > 0) {
			List<Object[]> list = linkFlatDetailsAndUserDetailsRepository.getLinkObjectDetails(userContext.getApartmentId(), id);
			System.out.println(list);
			if(list != null && !list.isEmpty()) {
				respMap.put("currentOwner", Utils.convertArrayToMap(list.get(0), new String[]{"id", "flatId", "userId", "firstName", "lastName", "contactNo1" }));
			}
			respMap.put("duesList", paymentDetailsService.getDuesList(id, new Object[] {}));
//			MaintenanceEntity maintenanceEntity = maintenanceRepository.get(userContext.getApartmentId(), userContext.getSessionId(), id);
//			respMap.put("maintenancePerMonth", maintenanceEntity != null && maintenanceEntity.getAmount() > 0? maintenanceEntity.getAmount() : 0);
			
			
			
			
			respMap.put("eventList", getEventList(id));
		}
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, respMap);
	}
	
	public List<EventsDTO> getEventList(Long flatId) {
		UserContext userContext = Utils.getUserContext();
		
		String flatType = flatDetailsRepository.getFlatType(userContext.getApartmentId(), flatId);
		List<EventsEntity> eventsEntityList = eventsRepository.getAllActiveEntity(userContext.getApartmentId());
		List<EventsDTO> eventsDtoList = Utils.convertList(eventsEntityList, EventsDTO.class);
		
		List<PaymentDetailsEntity> paymentDetails = paymentDetailsRepository.findByFlatIdForEventDueList(userContext.getApartmentId(), flatId);
		for(EventsDTO evntDto : eventsDtoList) {
			if(evntDto.getAmountPerFlat() != null) {
				double amountPerFlat = ApiConstants.FLAT_TYPE_DOUBLE.equals(flatType) ? (evntDto.getAmountPerFlat() * 2) : evntDto.getAmountPerFlat();
				double sum = paymentDetails.stream().filter(f -> f.getEventId().equals(evntDto.getId())).map(PaymentDetailsEntity::getAmount).mapToDouble(Double::doubleValue).sum();

				evntDto.setDueAmount(amountPerFlat - sum);				
			}
		}
		return eventsDtoList;
	}
	
	@Transactional
	@Override
	public ApiResponseEntity save(PaymentSaveDTO paymentSaveDTO) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		
		if(paymentSaveDTO != null) {			
			Long userId = linkFlatDetailsAndUserDetailsRepository.getActiveLinkUserId(userContext.getApartmentId(), paymentSaveDTO.getFlatId());
			
			PaymentEntity paymentEntity = new PaymentEntity();
			BeanUtils.copyProperties(paymentSaveDTO, paymentEntity);
			paymentEntity.setBillNo(billNoService.getPaymentBillNo());
			
			double totalMaintenanceAmount = paymentSaveDTO.getMonthList().stream().map(PaymentDetailsDTO::getAmount).mapToDouble(Double::doubleValue).sum();
			
			double totalMisleniousAmount = 0;
			if(paymentSaveDTO.getMiscellaneousFields() != null && !paymentSaveDTO.getMiscellaneousFields().isEmpty()) {
				totalMisleniousAmount = paymentSaveDTO.getMiscellaneousFields().stream().mapToDouble(MiscellaneousFieldDTO::getValue).sum();
			}
			
			double totalAmount = totalMaintenanceAmount + totalMisleniousAmount;
			paymentEntity.setAmount(totalAmount);
			paymentEntity.setPaymentBy(userId);
			paymentEntity.setCreatedBy(userContext.getUserId());
			paymentEntity.setModifiedBy(userContext.getUserId());
			paymentEntity.setSessionId(userContext.getSessionId());
			paymentEntity.setApartmentId(userContext.getApartmentId());
			paymentRepository.save(paymentEntity);
			
			if(paymentEntity.getId() <= 0) {
				throw new Exception("Maintenance not saved....!");
			}

			//Duplicate payment checking
			paymentDetailsService.isDuplicatePaymentDetailsFound(paymentSaveDTO.getMonthList(), paymentSaveDTO.getFlatId());
			
			PaymentDetailsEntity paymentDetailsEntity = null;
			for(PaymentDetailsDTO maintenanceDetailsDto : paymentSaveDTO.getMonthList()) {
				paymentDetailsEntity = new PaymentDetailsEntity();
				BeanUtils.copyProperties(maintenanceDetailsDto, paymentDetailsEntity, "id");
				paymentDetailsEntity.setPaymentId(paymentEntity.getId());
				paymentDetailsEntity.setPaymentBy(paymentEntity.getPaymentBy());
				paymentDetailsEntity.setEventId(new Long(1));
				paymentDetailsEntity.setPaymentDate(paymentEntity.getPaymentDate());
				paymentDetailsEntity.setPaymentForSessionId(maintenanceDetailsDto.getPaymentForSessionId());
				paymentDetailsEntity.setCreatedBy(userContext.getUserId());
				paymentDetailsEntity.setModifiedBy(userContext.getUserId());
				paymentDetailsEntity.setSessionId(userContext.getSessionId());
				paymentDetailsEntity.setApartmentId(userContext.getApartmentId());
				paymentDetailsRepository.save(paymentDetailsEntity);
			}
			for(MiscellaneousFieldDTO miscFld : paymentSaveDTO.getMiscellaneousFields()) {
				if(miscFld != null) {
					paymentDetailsEntity = new PaymentDetailsEntity();
					paymentDetailsEntity.setPaymentId(paymentEntity.getId());
					paymentDetailsEntity.setPaymentBy(paymentEntity.getPaymentBy());
					paymentDetailsEntity.setEventId(miscFld.getEventId());
					paymentDetailsEntity.setAmount(miscFld.getValue());
					paymentDetailsEntity.setFlatId(paymentEntity.getFlatId());
					paymentDetailsEntity.setPaymentDate(paymentEntity.getPaymentDate());
					paymentDetailsEntity.setPaymentForSessionId(Long.valueOf(0));
					paymentDetailsEntity.setCreatedBy(userContext.getUserId());
					paymentDetailsEntity.setModifiedBy(userContext.getUserId());
					paymentDetailsEntity.setSessionId(userContext.getSessionId());
					paymentDetailsEntity.setApartmentId(userContext.getApartmentId());
					paymentDetailsRepository.save(paymentDetailsEntity);
				}
			}
			
						
			String noteText = "Payment received By: " + userContext.getUserFullName();
			notesService.createSystemNote(ApiConstants.OBJECT_PAYMENT, paymentEntity.getId(), "PAYMENT", noteText);
			
			noteText = "Payment received By: " + userContext.getUserFullName()
						+ "\n Bill No: " + paymentEntity.getBillNo() + " and Amount: " + paymentEntity.getAmount();
			notesService.createSystemNote(ApiConstants.OBJECT_FLAT_DETAILS, paymentEntity.getFlatId(), "PAYMENT", noteText);
			
			paymentSaveDTO.setId(paymentEntity.getId());
		}
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, paymentSaveDTO);
	}
	
	
	@Transactional
	@Override
	public ApiResponseEntity deleteById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		
		if(id != null && id > 0) {
			paymentDetailsRepository.deletePaymentDetailsByMaintenanceId(userContext.getApartmentId(),userContext.getSessionId(),id);
			paymentRepository.deleteById(id);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity get(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			PaymentEntity entity = paymentRepository.getById(id);
			if(entity != null) {
				PaymentDTO dto = new PaymentDTO();
				BeanUtils.copyProperties(entity, dto);
				
				FlatDetailsEntity flatDetailsEntity = flatDetailsRepository.findById(entity.getFlatId()).orElse(null);
				if(flatDetailsEntity != null) {
					dto.setFlatNo(flatDetailsEntity.getFlatNo());
				}
				
				UserDetailsEntity userDetailsEntity = userDetailsRepository.getById(entity.getPaymentBy());
				if(userDetailsEntity != null) {
					dto.setPaymentByName(userDetailsEntity.getFirstName() + " " + userDetailsEntity.getLastName());
				}
				
				commonService.addUserDetailsToDTO(dto, PaymentDTO.class);
				
				
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dto);
			} else {
				throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public File getFileToDownload(Long id) {
		UserContext userContext = Utils.getUserContext();
		
		
		File file = null;
		if(id != null && id > 0) {
			Map<String, Object> result = new HashMap<>();
			PaymentEntity entity = paymentRepository.getById(id);
			if(entity != null) {
				List<PaymentDetailsEntity> paymentItemList = paymentDetailsRepository.findByPaymentId(userContext.getApartmentId(), userContext.getSessionId(), id);
				
				SessionDetailsEntity sessionDetailsEntity = sessionDetailsRepository.findById(entity.getSessionId()).orElse(null);
				if(sessionDetailsEntity != null) {
					result.put("sessionName", sessionDetailsEntity.getName());
				}
				
				FlatDetailsEntity flatDetailsEntity = flatDetailsRepository.findById(entity.getFlatId()).orElse(null);
				if(flatDetailsEntity != null) {
					result.put("flatNo", flatDetailsEntity.getFlatNo());
				}
				
				List<Object[]> list = linkFlatDetailsAndUserDetailsRepository.getLinkObjectDetails(userContext.getApartmentId(), entity.getFlatId()); 
				if(list != null && !list.isEmpty()) {
					result.put("mobileNo", list.get(0)[5]);
					result.put("ownersName", list.get(0)[3] + " " +list.get(0)[4]);
				}
				
				List<EventsEntity> eventList = eventsRepository.findAll();
				Map<Long, String> eventListMap = eventList.stream().collect(Collectors.toMap(EventsEntity::getId, EventsEntity::getName));
				
				String filePath = FileUtils.prepairFilePathForPaymentReceipt(userContext, FILE, entity, flatDetailsEntity);
				PaymentReceiptPDF pdf = new PaymentReceiptPDF(filePath, entity, paymentItemList, result, eventListMap);
				file = pdf.getFile();
				
			}
		} 
		return file;
	}

	@Transactional
	@Override
	public ApiResponseEntity cancel(@NonNull PaymentOrVoucharCancelDTO paymentCancel) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if (paymentCancel.getId() > 0) {
			PaymentEntity entity = paymentRepository.getById(paymentCancel.getId());
			if (entity != null) {
				if(entity.getIsCanceled() != null && entity.getIsCanceled()) {
					throw new RuntimeException("Payment already cancled.");
				}
				entity.setIsCanceled(true);
				entity.setCancelRemarks(paymentCancel.getCancelRemarks());
				entity.setModifiedBy(userContext.getUserDetailsEntity().getId());
				paymentRepository.save(entity);
				
				List<PaymentDetailsEntity> detailsList = paymentDetailsRepository.findByPaymentId(userContext.getApartmentId(), userContext.getSessionId(), entity.getId());
				for(PaymentDetailsEntity dtlsEntity : detailsList) {
					dtlsEntity.setIsCanceled(true);
					paymentDetailsRepository.save(dtlsEntity);
				}
				
				
				String noteText = "Cancel payment with below mention remarks,"
						+ "\n " + paymentCancel.getCancelRemarks()
						+ "\n Canceled By: " + userContext.getUserFullName();
				notesService.createSystemNote(ApiConstants.OBJECT_PAYMENT, entity.getId(), "CANCEL-PAYMENT", noteText);
				
				noteText = "Payment canceled By " + userContext.getUserFullName()
				+ " where bill No: " + entity.getBillNo() + " and amount: " + entity.getAmount();
				notesService.createSystemNote(ApiConstants.OBJECT_FLAT_DETAILS, entity.getFlatId(), "CANCEL-PAYMENT", noteText);
	
				
			} else {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}

		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
	}
	
	@Override
	public ApiResponseEntity getSlipByMonths() {
		UserContext userContext = Utils.getUserContext();
		List <PaymentSlipByMonthsDTO> dtoList = new ArrayList<PaymentSlipByMonthsDTO>();
		
		String sessionName = userContext.getSessionDetailsEntity().getName();
		String path = FILE + "/" + sessionName;
		File parentFolder = new File(path);
		File []folderAndFiles = parentFolder.listFiles();
		
		for (File file : folderAndFiles) {
			if(file.isDirectory()) {
				PaymentSlipByMonthsDTO ps = new PaymentSlipByMonthsDTO();
				ps.setFolderName(file.getName());
				ps.setFolderPath(path+"/"+file.getName());
				dtoList.add(ps);
			}
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dtoList);
	}
	
	@Override
	public File downloadZip (String folderName) throws Exception {
		UserContext userContext = Utils.getUserContext();
		maintenanceExcelByMonth(folderName);
		return ZipFileUtils.createZip(userContext, FILE, folderName, "payment");
	}
	
	public void maintenanceExcelByMonth (String folderName) {
		UserContext userContext = Utils.getUserContext();

		Date firstDay = DateUtils.stringToDate("01-"+folderName);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(firstDay);
		int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
		Date lastDay = calendar.getTime();
		
		String startDt = DateUtils.dateToStringForDB(firstDay) + " 00:00:00";
		String endDt = DateUtils.dateToStringForDB(lastDay) + " 23:59:59";
		List<Object[]> list = paymentRepository.getMonthlyPaymentList(userContext.getApartmentId(), userContext.getSessionId(), DateUtils.stringToDateTimeForDB(startDt), DateUtils.stringToDateTimeForDB(endDt));
		
		List<PaymentDTO> listDto = new ArrayList<>();
		for(Object[] arr : list) {
			PaymentEntity entity = (PaymentEntity)arr[0];
			String flatNo = (String)arr[1];
			String createdBy=(String)arr[2];
			String paymentBy = (String)arr[3];
			PaymentDTO dto = new PaymentDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.setFlatNo(flatNo);
			dto.setCreatedByName(createdBy);
			dto.setPaymentByName(paymentBy);
			
			listDto.add(dto);
		}
		
		
		
		String path = FILE;
		// Create dir for Session
		String sessionName = userContext.getSessionDetailsEntity().getName();
		path = path + "/" + sessionName;
		
		// Month wise dir creation.....		
		path = path + "/" + folderName;
		
		Path fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
            String fileName = path + "/" +  "payment-receipt_"+ folderName;
    		List<Object> headerFields = Arrays.asList(new String[] {"Srl No", "Bill No", "Flat Id", "Amount", "Payment Mode", "Payment Mode Ref", "Payment Date", "Payment By", "Is Canceled", "Cancel Remarks", "Created Date", "Created By"}) ; 
    		ExcelFileUtils.createExcelSheet(headerFields, listDto, folderName, fileName);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
	}
}

