package com.example.goldenticketnew.cgvadmin.service;

import com.example.goldenticketnew.cgvadmin.dto.request.CgvMovieRequest;
import com.example.goldenticketnew.cgvadmin.dto.response.CgvMovieDTO;
import com.example.goldenticketnew.cgvadmin.repository.CgvScheduleReadRepository;
import com.example.goldenticketnew.model.Movie;
import com.example.goldenticketnew.repository.IMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CgvMovieService {

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private CgvScheduleReadRepository scheduleReadRepository;

    public Page<CgvMovieDTO> getAllMovies(String search, Pageable pageable) {
        List<Movie> movies;
        if (search != null && !search.trim().isEmpty()) {
            movies = movieRepository.findAll()
                    .stream()
                    .filter(m -> m.getName() != null && m.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            movies = movieRepository.findAll();
        }

        List<CgvMovieDTO> dtos = movies.stream()
                .map(CgvMovieDTO::new)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<CgvMovieDTO> subList = start > dtos.size() ? List.of() : dtos.subList(start, end);

        return new PageImpl<>(subList, pageable, dtos.size());
    }

    public CgvMovieDTO getMovieDetail(Integer id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));
        return new CgvMovieDTO(movie);
    }

    public CgvMovieDTO createMovie(CgvMovieRequest request) {
        Movie movie = new Movie();
        mapRequestToEntity(request, movie);
        return new CgvMovieDTO(movieRepository.save(movie));
    }

    public CgvMovieDTO updateMovie(Integer id, CgvMovieRequest request) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));
        mapRequestToEntity(request, movie);
        return new CgvMovieDTO(movieRepository.save(movie));
    }

    public void deleteMovie(Integer id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));
        
        if (scheduleReadRepository.existsByMovieId(id)) {
            throw new RuntimeException("Không thể xóa: Phim này đang có suất chiếu liên kết");
        }

        try {
            movieRepository.delete(movie);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xóa phim: " + e.getMessage());
        }
    }

    private void mapRequestToEntity(CgvMovieRequest request, Movie movie) {
        movie.setName(request.getName());
        movie.setSmallImageURl(request.getSmallImageURl());
        movie.setShortDescription(request.getShortDescription());
        movie.setLongDescription(request.getLongDescription());
        movie.setLargeImageURL(request.getLargeImageURL());
        movie.setDirector(request.getDirector());
        movie.setActors(request.getActors());
        movie.setCategories(request.getCategories());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setDuration(request.getDuration());
        movie.setTrailerURL(request.getTrailerURL());
        movie.setLanguage(request.getLanguage());
        movie.setRated(request.getRated());
        movie.setIsShowing(request.getIsShowing());
    }
}
