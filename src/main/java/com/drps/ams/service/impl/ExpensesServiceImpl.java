package com.drps.ams.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drps.ams.bean.ExpenseInfo;
import com.drps.ams.bean.PaymentInfo;
import com.drps.ams.bean.UserContext;
import com.drps.ams.dbquery.DBQueryExecuter;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ExpenseItemsDTO;
import com.drps.ams.dto.ExpensesDTO;
import com.drps.ams.dto.FlatDetailsDTO;
import com.drps.ams.dto.PaymentOrVoucharCancelDTO;
import com.drps.ams.dto.PaymentDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.dto.VoucherByMonthDTO;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.ExpenseItemsEntity;
import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
import com.drps.ams.entity.PaymentEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.exception.FileStorageException;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.exception.RecordCanceledException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.exception.UserContextNotFoundException;
import com.drps.ams.repository.ExpenseItemsRepository;
import com.drps.ams.repository.ExpensesRepository;
import com.drps.ams.repository.NotesRepository;
import com.drps.ams.repository.UserDetailsRepository;
import com.drps.ams.service.CommonService;
import com.drps.ams.service.ExpensesService;
import com.drps.ams.service.NotesService;
import com.drps.ams.service.UserDetailsService;
import com.drps.ams.service.VoucherNoService;
import com.drps.ams.util.Utils;
import com.drps.ams.util.ZipFileUtils;
import com.drps.pdf.ExpenseVoucherPDF;
import com.drps.pdf.PaymentReceiptPDF;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.DateUtils;
import com.drps.ams.util.DynamicQuery;
import com.drps.ams.util.ExcelFiles;
import com.drps.ams.util.FileUtils;
import com.drps.ams.util.ParameterVerifier;

import lombok.NonNull;

@Service
public class ExpensesServiceImpl implements ExpensesService {

	private static final Logger logger = LogManager.getLogger(ExpensesServiceImpl.class);
	
	@Value("${file.storage.path}")
	String storagePath;
	
	String VOUCHAR_PATH = "/vouchar";
	
	@Autowired
	ExpensesRepository expensesRepository;
	
	@Autowired
	ExpenseItemsRepository expenseItemsRepository;

	@Autowired
	CommonService commonService;
	
	@Autowired
	VoucherNoService voucherNoService;
	
	@Autowired
	DBQueryExecuter dbQueryExecuter;
	
	@Autowired
	NotesRepository notesRepository;
	
	@Autowired
	NotesService notesService;
	
	@Autowired
	UserDetailsService userDetailsService;

	@Transactional
	@Override
	public ApiResponseEntity saveOrUpdate(@NonNull ExpensesDTO expensesDTO) throws Exception {
		
		UserContext userContext = Utils.getUserContext();
		
		if(ApiConstants.LIST_OPTION_SELECT_VAL == expensesDTO.getEventId()) {
			expensesDTO.setEventId(null);
		}
		
		if(expensesDTO.getItems() != null && expensesDTO.getItems().size() > 0) {
			double mntnceTot = expensesDTO.getItems().stream().map(ExpenseItemsDTO::getAmount).collect(Collectors.summingDouble(Double::doubleValue));
			expensesDTO.setAmount(mntnceTot);
		} else {
			throw new RuntimeException("Item heads not found. Please add Item heads.");
		}
		
		ExpensesEntity expensesEntity = null;
		ExpenseItemsEntity expensesItemEntity = null;
		if(expensesDTO.getId() != null && expensesDTO.getId() > 0) {
			expensesEntity = expensesRepository.getById(expensesDTO.getId());
			BeanUtils.copyProperties(expensesDTO, expensesEntity, Utils.getIgnoreEntityPropsOnUpdate(new String[] {"id"}));
			expensesEntity.setModifiedBy(userContext.getUserDetailsEntity().getId());
			expensesRepository.save(expensesEntity);
			
			expenseItemsRepository.deleteByExpenseId(userContext.getApartmentId(), userContext.getSessionId(), expensesDTO.getId());
		} else {
			expensesEntity = new ExpensesEntity();
			BeanUtils.copyProperties(expensesDTO, expensesEntity);
			expensesEntity.setVoucherNo(voucherNoService.getPaymentVoucherNo());
			expensesEntity.setCreatedBy(userContext.getUserId());
			expensesEntity.setModifiedBy(userContext.getUserId());
			expensesEntity.setSessionId(userContext.getSessionId());
			expensesEntity.setApartmentId(userContext.getApartmentId());
			expensesRepository.save(expensesEntity);
			
		}
		
		if(expensesDTO.getItems() != null) {
			for(ExpenseItemsDTO item : expensesDTO.getItems()) {
				if(item != null) {
					expensesItemEntity = new ExpenseItemsEntity();
					BeanUtils.copyProperties(item, expensesItemEntity);
					expensesItemEntity.setExpenseId(expensesEntity.getId());
					expensesItemEntity.setExpenseDate(expensesEntity.getExpenseDate());
					expensesItemEntity.setCreatedBy(userContext.getUserDetailsEntity().getId());
					expensesItemEntity.setModifiedBy(userContext.getUserDetailsEntity().getId());
					expensesItemEntity.setSessionId(userContext.getSessionId());
					expensesItemEntity.setApartmentId(userContext.getApartmentId());
					expenseItemsRepository.save(expensesItemEntity);
				}
			}
		}
			
		BeanUtils.copyProperties(expensesEntity, expensesDTO);	
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, expensesDTO);
	}

	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);		
		List<ExpensesDTO> rtnList = dbQueryExecuter.executeQuery(new QueryMaker<ExpensesDTO>(reqParamDto, ExpensesDTO.class));
		commonService.addUserDetailsToDTO(rtnList, ExpensesDTO.class);
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public ApiResponseEntity get() {
		UserContext userContext = Utils.getUserContext();
		
		List<ExpensesEntity> list = expensesRepository.findAll();

		List<ExpensesDTO> rtnList = Utils.convertList(list, ExpensesDTO.class);
		commonService.addUserDetailsToDTO(rtnList, ExpensesDTO.class);
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}

	@Override
	public ApiResponseEntity getById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			ExpensesEntity expensesEntity = expensesRepository.findById(id).get();
			if(expensesEntity != null) {
				ExpensesDTO expensesDTO = new ExpensesDTO();
				BeanUtils.copyProperties(expensesEntity, expensesDTO);
				
				List<ExpenseItemsEntity> itemsList = expenseItemsRepository.findByExpenseId(userContext.getApartmentId(), userContext.getSessionId(), id);
				expensesDTO.setItems(Utils.convertList(itemsList, ExpenseItemsDTO.class));
				
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, expensesDTO);
			} else {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	@Transactional
	public ApiResponseEntity deleteById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			expenseItemsRepository.deleteByExpenseId(userContext.getApartmentId(), userContext.getSessionId(), id);
			expensesRepository.deleteById(id);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity deleteAllById(List<Long> ids) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(ids != null && ids.size() > 0) {
			expensesRepository.deleteAllById(ids);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public File getFileToDownload(Long id) {
		UserContext userContext = Utils.getUserContext();
//		File file = new File("C:/Users/002ZX2744/Desktop/TEMP/pdf/expanse-voucher.pdf");
		File file = null;
		if(id != null && id > 0) {
			Map<String, Object> result = new HashMap<>();
			ExpensesEntity entity = expensesRepository.findByExpenseId(userContext.getApartmentId(), id);
			if(entity != null) {
				List<ExpenseItemsEntity> expenseItemList = expenseItemsRepository.findByExpenseId(userContext.getApartmentId(), userContext.getSessionId(), id);
				
				String path = FileUtils.getApplicationBaseFilePath(userContext, storagePath, true);
				path = path + VOUCHAR_PATH;
				String filePath = FileUtils.prepairFilePathForVouchar(userContext, path, entity);
//				String sigPath = FileUtils.getSignatureFilePath(userContext.getUserId(), userContext.getApartmentId(), storagePath);					
				String treasurerSignPath = FileUtils.getSignatureFilePath(entity.getApprovedByTrsId(), userContext.getApartmentId(), storagePath);
				String secretarySignPath = FileUtils.getSignatureFilePath(entity.getApprovedbySecId(), userContext.getApartmentId(), storagePath);
				ExpenseVoucherPDF pdf = new ExpenseVoucherPDF(filePath, entity, expenseItemList, result, null, treasurerSignPath, secretarySignPath);
				file = pdf.getFile();
			}
		}
		
		return file;
	}
	
	@Transactional
	@Override
	public ApiResponseEntity cancel(@NonNull PaymentOrVoucharCancelDTO cancel) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if (cancel.getId() > 0) {
			ExpensesEntity entity = expensesRepository.getById(cancel.getId());
			if (entity != null) {
				if(entity.getIsCanceled() != null && entity.getIsCanceled()) {
					throw new RuntimeException("Vouchar already cancled.");
				}
				entity.setIsCanceled(true);
				entity.setCancelRemarks(cancel.getCancelRemarks());
				entity.setModifiedBy(userContext.getUserDetailsEntity().getId());
				expensesRepository.save(entity);
				
				List<ExpenseItemsEntity> itemsList = expenseItemsRepository.findByExpenseId(userContext.getApartmentId(), userContext.getSessionId(), entity.getId());
				for(ExpenseItemsEntity itemsEntity : itemsList) {
					itemsEntity.setIsCanceled(true);
					expenseItemsRepository.save(itemsEntity);
				}
				
				String noteText = "Cancel vouchar with below mention remarks,"
						+ "\n " + cancel.getCancelRemarks()
						+ "\n Canceled By: " + userContext.getUserFullName();
				notesService.createSystemNote(ApiConstants.OBJECT_EXPENSE, entity.getId(), "CANCEL-VOUCHAR", noteText);
				
				if(entity.getEventId() != null && entity.getEventId() > 0) {
					noteText = "Vouchar canceled By " + userContext.getUserFullName()
					+ " where vouchar No: " + entity.getVoucherNo() + " and amount: " + entity.getAmount();
					notesService.createSystemNote(ApiConstants.OBJECT_EVENTS, entity.getEventId(), "CANCEL-VOUCHAR", noteText);
				}
			} else {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
	}
	
	@Override
	public ApiResponseEntity getVoucherByMonths() {
		UserContext userContext = Utils.getUserContext();
		List <VoucherByMonthDTO> dtoList = new ArrayList<VoucherByMonthDTO>();
		
		String path = FileUtils.getApplicationBaseFilePath(userContext, storagePath, true);
		path = path + VOUCHAR_PATH;
		
		File parentFolder = new File(path);
		File []folderAndFiles = parentFolder.listFiles();
		
		for (File file : folderAndFiles) {
			if(file.isDirectory()) {
				VoucherByMonthDTO ps = new VoucherByMonthDTO();
				ps.setFolderName(file.getName());
//				ps.setFolderPath(path+"/"+file.getName());
				dtoList.add(ps);
			}
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, dtoList);
	}
	
	@Override
	public File downloadZip (String folderName) throws Exception {
		UserContext userContext = Utils.getUserContext();
		String path = FileUtils.getApplicationBaseFilePath(userContext, storagePath, true);
		path = path + VOUCHAR_PATH;
		expensesExcelByMonth (path, folderName);
		return ZipFileUtils.createZip(userContext, path, folderName, "expense");
	}
	
	
	public void expensesExcelByMonth (String path, String folderName) {
		UserContext userContext = Utils.getUserContext();

		Date firstDay = DateUtils.stringToDate("01-"+folderName);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(firstDay);
		int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
		Date lastDay = calendar.getTime();
		
		String startDt = DateUtils.dateToStringForDB(firstDay) + " 00:00:00";
		String endDt = DateUtils.dateToStringForDB(lastDay) + " 23:59:59";
		List<Object[]> list = expensesRepository.getMonthlyExpensesList(userContext.getApartmentId(), userContext.getSessionId(), DateUtils.stringToDateTimeForDB(startDt), DateUtils.stringToDateTimeForDB(endDt));
		List<ExpenseItemsEntity> itemsList = expenseItemsRepository.getMonthlyExpenseItemsList(userContext.getApartmentId(), userContext.getSessionId(), DateUtils.stringToDateTimeForDB(startDt), DateUtils.stringToDateTimeForDB(endDt));

		List<ExpensesDTO> listDto = new ArrayList<>();
		for(Object[] arr : list) {
			ExpensesEntity entity = (ExpensesEntity)arr[0];
			String createdBy=(String)arr[1];
			String eventName = (String)arr[2]; 
			ExpensesDTO dto = new ExpensesDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.setCreatedByName(createdBy);
			dto.setEventName(eventName);
			
			listDto.add(dto);
		}
	

		List<ExpenseItemsDTO> itemsListDto = new ArrayList<>();
		for(ExpenseItemsEntity entity : itemsList) {
			ExpenseItemsDTO dto = new ExpenseItemsDTO();
			BeanUtils.copyProperties(entity, dto);
			
			itemsListDto.add(dto);
		}
		
		// Month wise dir creation.....		
		path = path + "/" + folderName;
		
		Path fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
            String fileName = path + "/" +  "voucher-receipt_"+ folderName;
                        
            ExcelFiles.expensesWithItems(listDto, itemsListDto, folderName, fileName);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
	}
	
	@Override
	public ExpenseInfo getExpenseInfo() {
		UserContext userContext = Utils.getUserContext();
		try {
			List<ExpensesEntity> list = expensesRepository.getAll(userContext.getApartmentId(), userContext.getSessionId());
			list = list.stream().filter( f ->(f.getIsCanceled() == null || !f.getIsCanceled()) && 
											ParameterVerifier.getLong(f.getApprovedbySecId()) > 0 &&
											ParameterVerifier.getLong(f.getApprovedByTrsId()) > 0
										).collect(Collectors.toList());
			
			double total = list.stream().map(ExpensesEntity::getAmount).reduce(0.0, Double::sum);
			
			return new ExpenseInfo(total);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ApiResponseEntity expensesApproved (String role, Long expenseId) {
		UserContext context = Utils.getUserContext();

		if(expenseId != null && expenseId > 0) {
			ExpensesEntity entity = expensesRepository.findByExpenseId(context.getApartmentId(), expenseId);
			if (entity != null) {
				if(entity.getIsCanceled() == null || entity.getIsCanceled() == false) {
					if (role.equals(ApiConstants.USER_ROLE_SECRETARY)) {
						expensesRepository.secretaryApproved(context.getUserId(), context.getApartmentId(), expenseId);
					} else if (role.equals(ApiConstants.USER_ROLE_TREASURER)) {
						expensesRepository.treasurerApproved(context.getUserId(), context.getApartmentId(), expenseId);
					}
					return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, null); 
				} else {
					throw new RecordCanceledException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_CANCELED_EXCEPTION));
				}
			} else {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
		
	}
}
