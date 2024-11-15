package org.example.daos;

import org.example.Main;
import org.example.domain.Uploads;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UploadsDao {
    private final NamedParameterJdbcTemplate template;

    public UploadsDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public void save(Uploads uploads){
        String sql = "insert into uploads(originalname, generatedname, mimetype, size) values(:originalName, :generatedName, :mimeType, :size)";
        BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(uploads);
        template.update(sql, paramSource);
    }

    public Uploads findByGeneratedName(String filename) {
        String sql = "select * from uploads where generatedname = :generatedname";
        Map<String, Object> paramSource = Map.of("generatedname", filename);
        return template.queryForObject(sql, paramSource, BeanPropertyRowMapper.newInstance(Uploads.class));
    }
}
