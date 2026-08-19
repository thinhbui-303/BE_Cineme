package com.example.goldenticketnew.cgvadmin.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CgvMovieRequest {

    @NotBlank(message = "Tên phim không được để trống")
    private String name;

    private String smallImageURl;

    private String shortDescription;

    private String longDescription;

    private String largeImageURL;

    @NotBlank(message = "Đạo diễn không được để trống")
    private String director;

    @NotBlank(message = "Diễn viên không được để trống")
    private String actors;

    @NotBlank(message = "Thể loại không được để trống")
    private String categories;

    @NotNull(message = "Ngày khởi chiếu không được để trống")
    private LocalDate releaseDate;

    @NotNull(message = "Thời lượng không được để trống")
    private Integer duration;

    private String trailerURL;

    private String language;

    @NotBlank(message = "Phân loại không được để trống")
    private String rated;

    @NotNull(message = "Trạng thái chiếu không được để trống")
    private int isShowing;
}
