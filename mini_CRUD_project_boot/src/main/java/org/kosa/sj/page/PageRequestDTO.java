package org.kosa.sj.page;

import java.net.URLEncoder;
import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDTO {
	@Min(value = 1)
	@Positive
	@Default
	private int page = 1; 
	
	@Min(value = 10)
	@Max(value = 100)
	@Default
	private int size = 10; 
	
	private int done;
	private String [] types;
	private String keyword;
	private String owner;
	private LocalDate from;
	private LocalDate to;
	
	public int getSkip() {
		return (page - 1) * size;
	}
	
	public String getLink()  {
		return getParam(this.page);
	}
	
	public String getParam(int page)  {
		StringBuilder builder = new StringBuilder();
		builder.append("page=" + page);
		builder.append("&size=" + size);
		
		if (done >= 1) {
			builder.append("&done=" + done);
		}
		
		if (types != null && types.length > 0) {
			for (String type : types) {
				builder.append("&types=" + type);
			}
		}
		
		if (keyword != null && keyword.length() > 0) {
			try {
				builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (from != null) {
			builder.append("&from=" + from.toString());
		}
		
		if (to != null) {
			builder.append("&to=" + to.toString());
		}
		
		return builder.toString();
	}


}
