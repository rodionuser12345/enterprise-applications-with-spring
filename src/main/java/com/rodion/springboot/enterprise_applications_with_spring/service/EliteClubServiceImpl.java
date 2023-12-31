package com.rodion.springboot.enterprise_applications_with_spring.service;

import com.rodion.springboot.enterprise_applications_with_spring.dto.ClubDTO;
import com.rodion.springboot.enterprise_applications_with_spring.model.EliteClub;
import com.rodion.springboot.enterprise_applications_with_spring.repository.EliteClubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EliteClubServiceImpl implements EliteClubService {

    private final EliteClubRepository eliteClubRepository;
    private static final Logger LOG = LoggerFactory.getLogger(EliteClubServiceImpl.class);

    public EliteClubServiceImpl(EliteClubRepository eliteClubRepository) {
        this.eliteClubRepository = eliteClubRepository;
    }

    public List<ClubDTO> getAll() {
        return eliteClubRepository.findAll().stream().map(c -> new ClubDTO(c.getClubName())).collect(Collectors.toList());
    }

    public List<ClubDTO> searchClub(String searchTerm) {
        LOG.info("Searching term {}", searchTerm);
        List<ClubDTO> result = eliteClubRepository.findClubs(buildLikePattern(searchTerm)).stream().map(c -> new ClubDTO(c.getClubName())).collect(Collectors.toList());
        LOG.info("Search Result: {} ", result);
        return result;
    }

    private String buildLikePattern(String searchTerm) {
        return searchTerm.toLowerCase() + "%";
    }

    public void addClub(String... clubNames) {
        for (String clubName : clubNames) {
            EliteClub eliteClub = new EliteClub();
            eliteClub.setClubName(clubName);
            eliteClubRepository.save(eliteClub);
        }
    }

    @Override
    public ClubDTO getByID(long clubId) {
        return new ClubDTO(eliteClubRepository.getOne(clubId).getClubName());
    }

    @Override
    public void deleteClub(long clubId) {
        eliteClubRepository.deleteById(clubId);
    }

    @Override
    public ClubDTO updateClub(long clubId, ClubDTO updatedClub) {
        EliteClub eliteClub = new EliteClub();
        eliteClub.setId(clubId);
        eliteClub.setClubName(updatedClub.getClubName());
        final EliteClub saved = eliteClubRepository.save(eliteClub);
        return new ClubDTO(saved.getClubName());
    }
}
