package dev.spring_handsOn.crud.view.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.spring_handsOn.crud.services.ProductService;
import dev.spring_handsOn.crud.shared.ProductDTO;
import dev.spring_handsOn.crud.view.model.ProductRequest;
import dev.spring_handsOn.crud.view.model.ProductResponse;
import dev.spring_handsOn.crud_Helper.CSVHelper;
import dev.spring_handsOn.crud_message.ResponseMessage;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public ResponseEntity<List<ProductResponse>> findAll() {

		List<ProductDTO> products = productService.findAll();

		ModelMapper mapper = new ModelMapper();

		List<ProductResponse> response = products.stream()
				.map(ProductDTO -> mapper.map(ProductDTO, ProductResponse.class)).collect(Collectors.toList());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Optional<ProductResponse>> findById(@PathVariable Integer id) {

		Optional<ProductDTO> dto = productService.findById(id);

		ProductResponse product = new ModelMapper().map(dto.get(), ProductResponse.class);

		return new ResponseEntity<>(Optional.of(product), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest productRequest) {

		ModelMapper mapper = new ModelMapper();

		ProductDTO productDTO = mapper.map(productRequest, ProductDTO.class);

		productDTO = productService.addProduct(productDTO);

		return new ResponseEntity<>(mapper.map(productDTO, ProductResponse.class), HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		productService.delete(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> update(@RequestBody ProductRequest productRequest,
			@PathVariable Integer id) {
		ModelMapper mapper = new ModelMapper();

		ProductDTO productDTO = mapper.map(productRequest, ProductDTO.class);

		productDTO = productService.update(id, productDTO);

		return new ResponseEntity<>(mapper.map(productDTO, ProductResponse.class), HttpStatus.OK);
	}
	// file upload

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (CSVHelper.hasCSVFormat(file)) {
			try {
				productService.saveCSV(file);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				System.out.println(e);
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}

		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}

	/*
	 * @GetMapping("/tutorials") public ResponseEntity<List<ProductResponse>>
	 * getAllTutorials() { try { List<ProductResponse> tutorials =
	 * productService.getAllTutorials();
	 * 
	 * if (tutorials.isEmpty()) { return new
	 * ResponseEntity<>(HttpStatus.NO_CONTENT); }
	 * 
	 * return new ResponseEntity<>(tutorials, HttpStatus.OK); } catch (Exception e)
	 * { return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */

	// download CSV data

	@GetMapping("/download")
	public ResponseEntity<Resource> getFile() {

		String timeStamp = new SimpleDateFormat("yyyy_MM_dd hh:mm:ss").format(new java.util.Date());
		long getLength = timeStamp.length();
		if (timeStamp.equals("") || !(timeStamp.length() >= 19)) {
			System.out.println("Issue with Timestamp");
		}
		String filename = timeStamp + "_" + "PRODUCT.csv";
		InputStreamResource file = new InputStreamResource(productService.load());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/csv")).body(file);
	}

}
