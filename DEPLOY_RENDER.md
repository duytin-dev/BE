# Hướng dẫn deploy lên Render (free)

Stack: **Spring Boot** trên Render + **PostgreSQL** trên Neon (free).

---

## Tổng quan

```
GitHub repo  →  Render (Docker, chạy app)
                    ↓
              Neon PostgreSQL (database cloud)
```

Render **không** chạy `docker-compose` — chỉ deploy **app**. Database dùng Neon.

---

## Bước 1: Tạo database trên Neon

1. Vào https://neon.tech → **Sign up** / đăng nhập.
2. **New Project** → đặt tên (vd: `demo-ktpm`).
3. Vào project → **Dashboard** → tab **Connection Details**.
4. Chọn **JDBC** và copy các thông tin:

| Thông tin | Ví dụ |
|-----------|--------|
| Host | `ep-xxx-xxx.region.aws.neon.tech` |
| Database | `neondb` |
| User | `neondb_owner` |
| Password | *(copy password)* |

5. Ghép **JDBC URL** (bắt buộc có SSL):

```text
jdbc:postgresql://<HOST>/<DATABASE>?sslmode=require
```

Ví dụ:

```text
jdbc:postgresql://ep-cool-name-12345678.ap-southeast-1.aws.neon.tech/neondb?sslmode=require
```

Lưu 3 giá trị này — dùng ở Bước 4:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

---

## Bước 2: Đưa code lên GitHub

Trong thư mục `Demo` (có `pom.xml`, `Dockerfile`):

```bash
git init
git add .
git commit -m "prepare render deploy"
git branch -M main
git remote add origin https://github.com/<TEN-BAN>/<TEN-REPO>.git
git push -u origin main
```

> Nếu repo là monorepo (cả FE + BE), vẫn push được — khi tạo Render sẽ chỉ định **Root Directory** = `Demo`.

---

## Bước 3: Tạo Web Service trên Render

1. Vào https://dashboard.render.com → đăng nhập (có thể login bằng GitHub).
2. **New +** → **Web Service**.
3. **Connect** repository vừa push.
4. Điền form:

| Trường | Giá trị |
|--------|---------|
| **Name** | `tin-java-demo` (tùy ý) |
| **Region** | Singapore hoặc gần VN nhất |
| **Branch** | `main` |
| **Root Directory** | `Demo` *(chỉ khi repo nằm ở folder con; nếu repo chỉ có backend thì để trống)* |
| **Runtime** | **Docker** |
| **Instance Type** | **Free** |

Render tự đọc `Dockerfile` — không cần Build/Start command.

---

## Bước 4: Thêm Environment Variables

Trong Web Service → **Environment** → **Add Environment Variable**:

| Key | Value |
|-----|--------|
| `SPRING_DATASOURCE_URL` | JDBC từ Neon (có `?sslmode=require`) |
| `SPRING_DATASOURCE_USERNAME` | user Neon |
| `SPRING_DATASOURCE_PASSWORD` | password Neon |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` |
| `SPRING_JPA_DATABASE_PLATFORM` | `org.hibernate.dialect.PostgreSQLDialect` |
| `SPRING_JPA_SHOW_SQL` | `false` |

**Save Changes** → Render tự deploy lại.

---

## Bước 5: Đợi deploy & test

1. Tab **Logs** — đợi dòng tương tự:

```text
Started DemoApplication
```

2. Lấy URL: `https://tin-java-demo.onrender.com` (tên tùy bạn đặt).

3. Test API:

```bash
# GET
curl https://<TEN-APP>.onrender.com/api/v1/users

# POST
curl -X POST https://<TEN-APP>.onrender.com/api/v1/users \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Tin\",\"email\":\"tin@test.com\",\"address\":\"HN\"}"
```

PowerShell:

```powershell
Invoke-RestMethod -Uri "https://<TEN-APP>.onrender.com/api/v1/users"
```

---

## Deploy bằng Blueprint (tùy chọn)

Repo đã có file `render.yaml`. Trên Render:

**New +** → **Blueprint** → chọn repo → điền env DB Neon khi được hỏi.

---

## Lưu ý gói free Render

| Hiện tượng | Giải thích |
|------------|------------|
| Lần đầu mở API **chậm 30–60s** | App đang "wake up" sau khi sleep |
| Sau ~15 phút không traffic | Service sleep — bình thường với free |
| Build fail | Xem **Logs** → thường sai JDBC URL hoặc thiếu `sslmode=require` |

---

## Lỗi thường gặp

| Lỗi | Cách sửa |
|-----|----------|
| `Connection refused` / timeout DB | Kiểm tra JDBC URL, password, `?sslmode=require` |
| `password authentication failed` | Copy lại password từ Neon |
| Build Docker fail | Đảm bảo **Root Directory** trỏ đúng folder có `Dockerfile` |
| 404 trên `/` | Bình thường — API ở `/api/v1/users` |

---

## Cập nhật code sau này

```bash
git add .
git commit -m "update feature"
git push
```

Render tự build & deploy lại.

---

## Frontend (nếu có)

Trong FE, đổi base URL API thành:

```text
https://<TEN-APP>.onrender.com/api/v1
```

CORS: backend đang `@CrossOrigin(origins = "*")` — gọi từ FE hosted khác được.
