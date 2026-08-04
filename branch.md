# 项目结构

```
zhanxiu_web/
├── Backend_web_company/          # 后端 (Spring Boot)
│   └── src/main/java/
│       ├── controller/           # 控制器层
│       ├── service/               # 业务层
│       ├── mapper/                # 数据访问层
│       └── entity/                # 实体类
│
├── admin-system/                  # 后台管理 (Vue 3)
│   └── src/
│       ├── views/                 # 页面组件
│       ├── router/                # 路由配置
│       └── composables/           # 组合式函数
│
└── vue-project-web/               # 前台展示 (Vue 3 + TypeScript)
    └── src/
        ├── views/                 # 页面组件
        ├── components/            # 通用组件
        ├── api/                   # 接口请求
        └── router/                # 路由配置
```
