资源服务配置
1. 使用spring-cloud-oauth2资源服务配置
2. 网关中做认证，资源服务授权



## 方法级权限

单个权限访问
@PreAuthorize("hasAuthority('admin')")
@PreAuthorize("hasRole('admin')")
@Secured("admin")

必须同时拥有多个权限才能访问,加and
例:@PreAuthorize("hasAuthority('admin') and hasAuthority('super')")

拥有其中一个或多个权限才能访问,加or
例:@PreAuthorize("hasRole('admin') or hasRole('super')")
