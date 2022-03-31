public List<SomeDTO> getListDtos(Member member){
        return someService.getAccounts(member.getPartyId())
                .flatMapMany(Flux::fromIterable)
                .flatMap(account -> someOtherService.getAddresses(account)
                                    .flatMapMany(Flux::fromIterable)
                                    .filter(this::isUsAddress)
                                    .collectList()
                )
                .toStream()
                .map(addresses -> mapAddressesToSomeDTO(addresses))
                .collect(Collectors.toList());
    }
