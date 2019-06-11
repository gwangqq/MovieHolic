package com.kitri.dao.mypage;

import java.sql.*;
import java.util.*;

import com.kitri.dto.*;
import com.kitri.util.DBClose;
import com.kitri.util.DBConnection;

public class ReviewAddDao {

	private static ReviewAddDao reviewAdd;

	static {
		reviewAdd = new ReviewAddDao();
	}

	private ReviewAddDao() {

	}

	public static ReviewAddDao getReviewAdd() {
		return reviewAdd;

	}

	// 리뷰쓰기
	public int reviewAdd(BoardDto boardDto) {
		int cnt = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.makeConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("insert \n");
			sql.append(
					"	into holic_board (seq,userId,boardCode,subject,postDate, content,actor1,actor2, starPoint,movieName,movieCodeNaver,movieCodeYoung,category,enable) \n");
			sql.append("	values(seq.nextval,?,1,?,sysdate,?,?,?,?,?,?,?,?,1 ) \n");
			pstmt = conn.prepareStatement(sql.toString());
			int idx = 0;
			pstmt.setInt(++idx, boardDto.getSeq());
			pstmt.setString(++idx, boardDto.getUserId());
			pstmt.setInt(++idx, boardDto.getBoardCode());
			pstmt.setString(++idx, boardDto.getSubject());
			pstmt.setString(++idx, boardDto.getPostDate());
			pstmt.setString(++idx, boardDto.getContent());
			pstmt.setInt(++idx, boardDto.getStarPoint());
			pstmt.setString(++idx, boardDto.getActors());
			pstmt.setString(++idx, boardDto.getMovieName().toString());
			pstmt.setString(++idx, boardDto.getDirector().toString());
			pstmt.setString(++idx, boardDto.getMovieCodeYoung().toString());
			pstmt.setString(++idx, boardDto.getCategory());
			pstmt.setInt(++idx, boardDto.getEnable());

			cnt = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBClose.close(conn, pstmt);
		}

		return cnt;
	}

	// 리뷰목록
	public List<BoardDto> reviewlist(int startRow, int endRow) {
		
		List<BoardDto> list = new ArrayList<BoardDto>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnection.makeConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select  * \n");
			sql.append("from(select rownum, f.seq, f.userid, f.moviename, f.content, f.starpoint, f.d1, f.d2, f.d3, f.viewcount, f.enable \n");
			sql.append("		from(select seq,userid, movieName,content,starPoint,postDate d1, to_char(postDate, 'YYYY') d2, to_char(postDate, 'MM-DD') d3 , viewcount,enable \n");
			sql.append("				from mh_board \n");
			sql.append("				where boardCode = 1 \n");
			sql.append("				order by postDate desc) f \n");
			sql.append("		order by rownum) \n");
			sql.append("where rownum between ? and ? \n");
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardDto boardDto = new BoardDto();
					
				List<String> name = new ArrayList<String>();
				String str = rs.getString("movieName");
				String userid = rs.getString("userid");
				StringTokenizer st = new StringTokenizer(str, "||");
				StringTokenizer userid2 = new StringTokenizer(userid,"@");
				String id = userid2.nextToken();
				String a = st.nextToken();
				
				name.add(a);
				boardDto.setSeq(rs.getInt("seq"));
				boardDto.setUserId(id);
				boardDto.setViewCount(rs.getInt("viewcount"));
				boardDto.setContent(rs.getString("content"));
				boardDto.setStarPoint(rs.getInt("starPoint"));
				boardDto.setMovieName(name);
				boardDto.setPostDate(rs.getString("d1"));
				boardDto.setPostDateY(rs.getString("d2"));
				boardDto.setPostDateM(rs.getString("d3"));
				boardDto.setEnable(rs.getInt("enable"));
				
				list.add(boardDto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBClose.close(conn, pstmt, rs);
		}

		return list;
	}
	//리스트목록
public List<BoardDto> listList(String content) {
		
		List<BoardDto> list = new ArrayList<BoardDto>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnection.makeConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select subject,content \n");
			sql.append("from mh_board \n");
			sql.append("where boardCode =2 \n");
			sql.append("order by postDate desc \n");
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardDto boardDto = new BoardDto();
				boardDto.setContent(rs.getString("content"));

				list.add(boardDto);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBClose.close(conn, pstmt, rs);
		}

		return list;
	}


	//comment
	public List<CommentDto> reviewContent(String seq){
		List<CommentDto> list = new ArrayList<CommentDto>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnection.makeConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select seq,content,postDate d1, to_char(postDate, 'YYYY') d2, userId \n");
			sql.append("from mh_comment \n");
			sql.append("order by postDate desc \n");
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				CommentDto commentDto = new CommentDto();	
				String str = rs.getString("userid");
				StringTokenizer st = new StringTokenizer(str, "@");
				String a = st.nextToken();
				commentDto.setSeq(rs.getInt("seq"));
				commentDto.setUserId(a);
				commentDto.setContent(rs.getString("content"));
				commentDto.setPostDate(rs.getString("d1"));
				commentDto.setPostDateY(rs.getString("d2"));
				list.add(commentDto);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBClose.close(conn, pstmt, rs);
		}

		return list;
	}
	//리뷰정보
	public BoardDto selectByNo(String seq) {
		BoardDto boardDto = null;
//		CommentDto commentDto = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
	
		
		try {
			conn = DBConnection.makeConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select mb.seq,mf.movieimage, mb.movieName,mb.starPoint, mb.postdate,mc.postdate, to_char(mb.postdate, 'YYYY') d1, mb.content,mc.content, mb.userid,mc.userid , mb.viewcount, mb.enable \n");
			sql.append("from mh_board mb, mh_comment mc , mh_films mf \n");
			sql.append("where mb.boardcode = 1 \n");
			sql.append("and mb.seq = ?");
//			if("mb.content" != null) { 
//				sql.append("and mb.seq = mc.seq \n");
//			}
			sql.append("order by mb.postdate desc \n");
			
			pstmt= conn.prepareStatement(sql.toString());
			pstmt.setString(1, seq);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				boardDto = new BoardDto();
				List<String> mbmovie = new ArrayList<String>();
//				commentDto = new CommentDto();
				String mbid = rs.getString("userid");
				String name = rs.getString("movieName");
				StringTokenizer mb = new StringTokenizer(mbid, "@");
				StringTokenizer mbname = new StringTokenizer(name, "||");
				String mbid2 = mb.nextToken();
				String moviename = mbname.nextToken();
				mbmovie.add(moviename);
				
				boardDto.setSeq(rs.getInt("seq"));
				boardDto.setMovieName(mbmovie);
				boardDto.setPostDate(rs.getString("postdate"));
				boardDto.setPostDateY(rs.getString("d1"));
				boardDto.setContent(rs.getString("content"));
				boardDto.setUserId(mbid2);
				boardDto.setViewCount(rs.getInt("viewcount"));
				boardDto.setStarPoint(rs.getInt("starpoint"));
				boardDto.setEnable(rs.getInt("enable"));
				
//				commentDto.setPostDate(rs.getString("postdate"));
//				commentDto.setContent(rs.getString("content"));
//				commentDto.setUserId(mbid2);
				
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBClose.close(conn, pstmt, rs);
		}
		return boardDto;
	}
	//리뷰 총갯수
	public int selectTotalReview() {
		int cnt = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = DBConnection.makeConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select count(*) \n");
			sql.append("from mh_board \n");
			sql.append("where boardcode = 1 \n");
			sql.append("order by postdate desc \n");
			
			pstmt = conn.prepareStatement(sql.toString());
			
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
								
				cnt = rs.getInt(1);
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBClose.close(conn, pstmt, rs);
		}
		
		
		return cnt;
	}
	public UserDto selectId(String userid) {
		UserDto userDto =null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnection.makeConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select userid, name, pass, phonefirst, phonemid, phonelast, birth, gender \n");
			sql.append("from mh_user \n");
			sql.append("where userid = ? \n");
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				
				userDto = new UserDto();
				userDto.setUserId(rs.getString("userId"));
				userDto.setName(rs.getString("name"));
				userDto.setPass(rs.getString("pass"));
				userDto.setPhoneFirst(rs.getString("phonefirst"));
				userDto.setPhoneMid(rs.getString("phonemid"));
				userDto.setPhoneLast(rs.getString("phonelast"));
				userDto.setGender(rs.getString("gender"));
				userDto.setBirth(rs.getString("birth"));
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBClose.close(conn, pstmt, rs);
		}
		
		
		return userDto;
	}
	public static void main(String[] args) {
		
		System.out.println(getReviewAdd().reviewlist(1,1));
		System.out.println(getReviewAdd().listList("content"));
		System.out.println(getReviewAdd().reviewContent("seq"));
		System.out.println(getReviewAdd().selectByNo("seq"));
	}
}
