1.table schema  => h2根據entity自動生成


   //call 原始api
    @GetMapping("/bitcoin")

    //寫入db
    @PostMapping("/bitcoin/insert")

    //更新api  
    @GetMapping("/newapi")

    //crud
    @GetMapping("/bitcoin/{id}")
    @GetMapping("/bitcoin/all")
    @PutMapping("/bitcoin/{id}")
    @DeleteMapping("/bitcoin/{id}")
    
