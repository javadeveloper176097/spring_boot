package dev.spring_handsOn.crud.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.spring_handsOn.crud.model.Product;
import dev.spring_handsOn.crud.model.exception.ResourceNotFoundException;
import dev.spring_handsOn.crud.repository.ProductRepository;
import dev.spring_handsOn.crud.shared.ProductDTO;
import dev.spring_handsOn.crud_Helper.CSVHelper;
import dev.spring_handsOn.crud_Helper.CSVHelperDownload;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> findAll() {

        List<Product> products = productRepository.findAll();
        ProductDTO productDTO = new ProductDTO();
        Long count = productRepository.count();

        return products.stream()
                .map(product -> new ModelMapper()
                        .map(product, ProductDTO.class))
                .collect(Collectors.toList());
               
    }

    public Optional<ProductDTO> findById(int id) {

        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new ResourceNotFoundException("Id: " + id + " not found!");
        }

        ProductDTO dto = new ModelMapper().map(product.get(), ProductDTO.class);
        return Optional.of(dto);
    }

    // Here, we're gonna null the Id then set the id using the product.getId()
    // method over there.
    public ProductDTO addProduct(ProductDTO productDTO) {
        productDTO.setId(null);
        String suuid = UUID.randomUUID().toString();

        ModelMapper mapper = new ModelMapper();

        Product product = mapper.map(productDTO, Product.class);
        product.setUuid(suuid);
        product = productRepository.save(product);
        
        productDTO.setId(product.getId());
        productDTO.setUuid(product.getUuid());

        return productDTO;
    }

    public void delete(int id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new ResourceNotFoundException("Could not delete this product with id: " + id + ", product does not exist!");
        }

        productRepository.deleteById(id);
    }

    public ProductDTO update(int id, ProductDTO productDTO) {
        productDTO.setId(id);
     
        ModelMapper mapper = new ModelMapper();
        Product product = mapper.map(productDTO, Product.class);
        Optional<Product> product1 = productRepository.findById(id);
        String savedUUID = product1.get().getUuid();        
        product.setUuid(savedUUID);
        productRepository.save(product);
        
        productDTO.setUuid(product.getUuid());
        return productDTO;
    }
    
    //CSV file Upload
    
    public void saveCSV(MultipartFile file) {
        try {
          List<Product> tutorials = CSVHelper.csvToTutorials(file.getInputStream());
          productRepository.saveAll(tutorials);
        } catch (IOException e) {
          throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
      }

      public List<Product> getAllTutorials() {
        return productRepository.findAll();
      }
		// csv data download

		public ByteArrayInputStream load() {
			List<Product> tutorials = productRepository.findAll();

			ByteArrayInputStream in = CSVHelperDownload.tutorialsToCSV(tutorials);
			return in;
		}
}
