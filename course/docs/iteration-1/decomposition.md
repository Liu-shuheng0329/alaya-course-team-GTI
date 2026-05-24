# 迭代1 功能分解与 GWT 验收标准

## 接口契约（锁定，后续不得更改）

- 注册页：GET `/register`，表单提交 POST `/register`，参数名 `username`, `password`
- 登录页：GET `/login`，表单提交 POST `/login`（Spring Security 默认处理）
- 首页：GET `/home`
- 视图名称：`register`, `login`, `home`
- 错误信息在 Model 中的属性名：`error`
- 注册成功重定向：`/login?registered`

---

## US-1：注册学生账号

**业务规则**：
- 用户名必须唯一。
- 密码使用 BCrypt 加密存储。
- 注册成功后自动分配角色 `STUDENT`。

**约束条件**：
- 用户名不能为空。
- 密码不能为空（后端校验）。

**验收测试标准（GWT）**：
1. **正常**：Given 访问 `/register`，When 输入有效用户名 `student1` 和密码 `123456` 并提交 POST `/register`，Then 用户被保存到数据库，密码为 BCrypt 加密，角色为 `STUDENT`，重定向到 `/login?registered`。
2. **异常（用户名重复）**：Given 用户名 `student1` 已存在，When 再次使用 `student1` 注册，Then 显示“用户名已存在”错误（error 属性），用户未被创建。
3. **边界（密码为空）**：Given 访问 `/register`，When 密码字段为空并提交，Then 显示“密码不能为空”错误。

---

## US-2：用户登录

**业务规则**：
- 验证用户名和密码是否匹配。
- 登录失败提示“用户名或密码错误”（不区分用户不存在，防止枚举攻击）。

**约束条件**：
- 用户名密码均不能为空（前端+后端校验）。

**验收测试标准（GWT）**：
1. **正常**：Given 已注册学生 `student1`，When 输入正确密码提交 POST `/login`，Then 登录成功，跳转到 `/home`。
2. **异常（密码错误）**：Given 已注册学生 `student1`，When 输入错误密码，Then 显示“用户名或密码错误”。
3. **边界（用户不存在）**：Given 未注册的用户名，When 尝试登录，Then 显示“用户名或密码错误”。

---

## US-3：角色菜单与权限隔离

**业务规则**：
- 不同角色登录后首页显示不同的功能菜单。
- 学生只能访问 `/student/**` 路径，教师只能访问 `/teacher/**`，管理员只能访问 `/admin/**`。
- 未登录用户访问任何受保护路径时，自动重定向到 `/login`。

**约束条件**：
- 权限控制由 Spring Security 实现，使用 `hasAuthority` 而非 `hasRole`。

**验收测试标准（GWT）**：
1. **正常（学生菜单）**：Given 学生已登录，When 访问 `/home`，Then 页面显示学生菜单（“浏览课程”、“我的课表”）。
2. **异常（越权访问）**：Given 教师已登录，When 访问 `/student/courses`，Then 返回 HTTP 403 禁止访问。
3. **边界（未登录访问保护页面）**：Given 未登录用户，When 访问 `/student/courses`，Then 重定向到 `/login`。