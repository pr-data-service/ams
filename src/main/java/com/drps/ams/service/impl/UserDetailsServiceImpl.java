package com.drps.ams.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import com.drps.ams.bean.UserContext;
import com.drps.ams.cache.FlatDetailsCacheService;
import com.drps.ams.dbquery.DBQueryExecuter;
import com.drps.ams.dbquery.QueryMaker;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.ExpenseItemsDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.dto.UserDetailsDTO;
import com.drps.ams.dto.UserPasswordDTO;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.exception.DuplicateRecordException;
import com.drps.ams.exception.InvalidConfirmPassword;
import com.drps.ams.exception.InvalidCredentialsException;
import com.drps.ams.exception.NoRecordFoundException;
import com.drps.ams.exception.RecordIdNotFoundException;
import com.drps.ams.repository.LinkFlatDetailsAndUserDetailsRepository;
import com.drps.ams.repository.UserDetailsRepository;
import com.drps.ams.service.UserDetailsService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.FileUtils;
import com.drps.ams.util.Utils;

import lombok.NonNull;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Value("${file.storage.path}")
	String FILE;
	
	String SIGNATURE_PATH = "/user-signature";
	
	@Autowired
	UserDetailsRepository userDetailsRepository;
	
	@Autowired
	LinkFlatDetailsAndUserDetailsRepository linkFlatDetailsAndUserDetailsRepository;
	
	@Autowired
	DBQueryExecuter dbQueryExecuter;
	
	@Autowired
	FlatDetailsCacheService flatDetailsCacheService;

	@Override
	public ApiResponseEntity saveOrUpdate(@NonNull UserDetailsDTO userDetailsDTO) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		
		if(isDuplicateRecord(userDetailsDTO)) {
			throw new DuplicateRecordException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION));
		}
		
		
		UserDetailsEntity userDetailsEntity = null;
		if(userDetailsDTO.getId() != null && userDetailsDTO.getId() > 0) {
			userDetailsEntity = userDetailsRepository.findById(userDetailsDTO.getId()).get();
			BeanUtils.copyProperties(userDetailsDTO, userDetailsEntity, Utils.getIgnoreEntityPropsOnUpdate(null));
			userDetailsEntity.setModifiedBy(userContext.getUserDetailsEntity().getId());
		} else {
			userDetailsEntity = new UserDetailsEntity();
			BeanUtils.copyProperties(userDetailsDTO, userDetailsEntity);
			userDetailsEntity.setCreatedBy(userContext.getUserId());
			userDetailsEntity.setModifiedBy(userContext.getUserId());
			userDetailsEntity.setApartmentId(userContext.getApartmentId());
		}
		
			
		userDetailsRepository.save(userDetailsEntity);
		//Removing cache
		flatDetailsCacheService.removeCacheByApartmentId(userContext.getApartmentId());	
		
		BeanUtils.copyProperties(userDetailsEntity, userDetailsDTO);	
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, userDetailsDTO);
	}

	@Override
	public ApiResponseEntity getListView(String reqParams) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		List<ExpenseItemsDTO> rtnList = dbQueryExecuter.executeQuery(new QueryMaker<UserDetailsDTO>(reqParamDto, UserDetailsDTO.class));
		
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}

	@Override
	public ApiResponseEntity getById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			UserDetailsEntity userDetailsEntity = userDetailsRepository.findById(id).get();
			if(userDetailsEntity != null) {
				UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
				BeanUtils.copyProperties(userDetailsEntity, userDetailsDTO);
				return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, userDetailsDTO);
			} else {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity deleteById(Long id) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(id != null && id > 0) {
			userDetailsRepository.deleteById(id);
			//Removing cache
			flatDetailsCacheService.removeCacheByApartmentId(userContext.getApartmentId());	
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity deleteAllById(List<Long> ids) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(ids != null && ids.size() > 0) {
			userDetailsRepository.deleteAllById(ids);
			//Removing cache
			flatDetailsCacheService.removeCacheByApartmentId(userContext.getApartmentId());
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
	}

	@Override
	public ApiResponseEntity get() {
		UserContext userContext = Utils.getUserContext();
				
		List<UserDetailsEntity> list = userDetailsRepository.findAll();

		List<UserDetailsDTO> rtnList = new ArrayList<>();
		if (list != null) {
			UserDetailsDTO userDetailsDTO = null;
			for (UserDetailsEntity userDetailsEntity : list) {
				userDetailsDTO = new UserDetailsDTO();
				BeanUtils.copyProperties(userDetailsEntity, userDetailsDTO);
				rtnList.add(userDetailsDTO);
			}
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, rtnList);
	}
	
	@Override
	public boolean isDuplicateRecord(UserDetailsDTO userDetailsDTO) {
		List<UserDetailsEntity> list = userDetailsRepository.findByContactNo1(userDetailsDTO.getContactNo1());
		if(list != null && userDetailsDTO.getId() != null && userDetailsDTO.getId() > 0) {
			list = list.stream().filter( f -> f.getId() != userDetailsDTO.getId()).collect(Collectors.toList());
		}
		
		return list != null && list.size() > 0 ? true : false;
	}
	
	@Override
	public ApiResponseEntity getByFlatId(Long flatId) throws Exception {
		UserContext userContext = Utils.getUserContext();
		if(flatId != null && flatId > 0) {
			Long userId = linkFlatDetailsAndUserDetailsRepository.getActiveLinkUserId(userContext.getApartmentId(), flatId);
		
			if(userId != null && userId > 0) {
				UserDetailsEntity userDetailsEntity = userDetailsRepository.findById(userId).get();
				if(userDetailsEntity != null) {
					UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
					BeanUtils.copyProperties(userDetailsEntity, userDetailsDTO);
					return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, userDetailsDTO);
				} else {
					throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
				}
			} else {
				throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
			}
		} else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}		
	}
	
	@Override
	public ApiResponseEntity getLoggedInUder() throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		UserDetailsEntity userDetailsEntity = userContext.getUserDetailsEntity();
		if(userDetailsEntity != null) {
			UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
			BeanUtils.copyProperties(userDetailsEntity, userDetailsDTO);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, userDetailsDTO);
		} else {
			throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
		}
	}
	
	@Override
	public ApiResponseEntity updatePassword (UserPasswordDTO userPasswordDto) throws Exception {
		UserContext userContext = Utils.getUserContext();
		
		if(!userPasswordDto.getNewPassword().equals(userPasswordDto.getConfirmPassword())) {
			throw new InvalidConfirmPassword(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_INVALID_CONFIRM_PASSWORD_EXCEPTION));
		}
		
		Long id = userPasswordDto.getId();
		if(id!=null && id > 0) {
			UserDetailsEntity userDetailsEntity = userDetailsRepository.getById(id);
			
			if(userDetailsEntity != null) {
				
				if(userDetailsEntity.getPassword().equals(userPasswordDto.getOldPassword())) {
					userDetailsEntity.setPassword(userPasswordDto.getNewPassword());
					userDetailsRepository.save(userDetailsEntity);
					return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, "password Saved Successfully");
				}
				else {
					throw new InvalidCredentialsException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_INVALID_CREDENTIALS_EXCEPTION));
				}
			}
			else {
				throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
			}
		}
		else {
			throw new RecordIdNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION));
		}
		
	}
	
	@Override
	public ApiResponseEntity uploadSignature(MultipartFile file) throws IOException {
		
		UserContext userContext = Utils.getUserContext();

		if(Arrays.asList("image/jpg", "image/jpeg", "image/png").contains(file.getContentType())) {
			String path = FileUtils.getApplicationBaseFilePath(userContext, FILE, false);
			path = path + SIGNATURE_PATH;
			
			Path fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();
			Files.createDirectories(fileStorageLocation);
			
			String fileName = "signature_" + userContext.getUserId() +".jpg";  			
			
			Path root = Paths.get(path + "/" +fileName);
			Files.copy(file.getInputStream(), root,  StandardCopyOption.REPLACE_EXISTING);
			return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, "Signature Uploaded Successfully");
		} else {
			throw new UnsupportedMediaTypeStatusException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_UNSUPPORTED_MEDIA_TYPE)); 
		}
	}
	
	@Override
	public void getSignature(HttpServletRequest req, HttpServletResponse res) {
		
		UserContext userContext = Utils.getUserContext();
		
		String path = FileUtils.getApplicationBaseFilePath(userContext, FILE, false);
		path = path + SIGNATURE_PATH;
		
		String fileName = path+"/signature_" + userContext.getUserId();
		List<File> fileList = List.of(new File(fileName + ".jpg"),new File(fileName + ".jpeg"),	new File(fileName + ".png"));
		File image = fileList.stream().filter(File::exists).findFirst().orElse(null);
		
		if(image == null || !image.exists()) {
			throw new NoRecordFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION));
		}
		try {
			Utils.downloadPdfFile(req, res, image);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
