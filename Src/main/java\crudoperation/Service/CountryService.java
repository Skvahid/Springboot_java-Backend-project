package com.ums.service;

import com.ums.entity.Country;
import com.ums.payload.CountryDto;
import com.ums.repository.CountryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {
    private CountryRepository countryRepository;
    private ModelMapper modelMapper;

    public CountryService(CountryRepository countryRepository, ModelMapper modelMapper) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
    }

    public List<CountryDto> getAllCountries(){
        List<Country> countries = countryRepository.findAll();
        List<CountryDto> dtos = countries.stream().map(c -> convertToDto(c)).collect(Collectors.toList());
          return dtos;
    }
    CountryDto convertToDto(Country country){
        CountryDto countryDto  =  modelMapper.map(country,CountryDto.class);
        return countryDto;
    }
}
