package com.example.alumni.service;

import com.example.alumni.dto.AlumniProfileDto;
import com.example.alumni.dto.SearchRequest;
import com.example.alumni.entity.AlumniProfile;
import com.example.alumni.repository.AlumniProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlumniService {

    private final AlumniProfileRepository repository;
    //private final PhantomBusterClient phantomBusterClient;
    private final RehanApiCall   rehanApiCall;

    @Transactional
    public String searchAndSave(SearchRequest req) {
      //  List<AlumniProfile> fetched = phantomBusterClient.searchAlumniApi(req);
         String  fetched = rehanApiCall.searchAlumniApi(req).toString();
       // List<AlumniProfile> fetched = rehanApiCall.searchAlumniStatus(req);
      //  List<AlumniProfile> fetched = rehanApiCall.searchAlumniFetch(req);

      //String fetched = rehanApiCall.searchAlumniJSONFetch(req);


      //  List<AlumniProfile> saved = repository.saveAll(fetched);
      //  return saved.stream().map(AlumniProfileDto::from).collect(Collectors.toList());
        return fetched;
    }

    @Transactional(readOnly = true)
    public List<AlumniProfileDto> getAll() {
        return repository.findAll()
                .stream()
                .map(AlumniProfileDto::from)
                .collect(Collectors.toList());
    }

}