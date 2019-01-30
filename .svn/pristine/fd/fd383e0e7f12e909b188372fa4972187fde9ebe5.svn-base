
insert into users(user_id,user_name) values(20199999999999,'公司用户');
insert into user_wallet(user_id,currency_type) values(20199999999999,'USDT');

insert into payment_rule(payment_type,limit_volume,limit_number,payment_name,create_time,rotate_interval_max,rotate_interval_min)
values(1,30000,0,'支付宝(个人)',now(),1200,600);
insert into payment_rule(payment_type,limit_volume,limit_number,payment_name,create_time,rotate_interval_max,rotate_interval_min)
values(2,30000,0,'微信(个人)',now(),1200,600);
insert into payment_rule(payment_type,limit_volume,limit_number,payment_name,create_time,rotate_interval_max,rotate_interval_min)
values(3,30000,9,'银行卡',now(),1200,600);
insert into payment_rule(payment_type,limit_volume,limit_number,payment_name,create_time,rotate_interval_max,rotate_interval_min)
values(4,30000,9,'支付宝(企业)',now(),1200,600);

insert into payment_rotate(payment_id,status,interval_min,interval_max,limit_volume,limit_number)
select payment_id,1,pr.rotate_interval_min,pr.rotate_interval_max,pr.limit_volume,pr.limit_number from payment_mode pm,payment_rule pr where pm.payment_type=pr.payment_type;


CREATE DEFINER=`root`@`%` PROCEDURE `refineTimeoutOrder`(timeOut int)
BEGIN

    declare tradeNumber varchar(28);
    declare createTime ,paymentTime datetime default null;
    declare currentTime datetime;
		declare actualAmount, currencyAmount decimal(22,8);
		declare userWalletId, userId,tempId,merchantUserId,paymentId bigint; 	
		declare currencyType,orderType,orderStatus TINYINT; 
		declare t_error,hasRows INTEGER DEFAULT 0;  	
    declare next_cur CURSOR FOR SELECT od.trade_number,od.create_time,payment_time,od.type,od.`status`,merchant_user_id,user_id,actual_amount,currency_amount,currency_type,payment_id FROM orders od where od.status <2 and od.type=0  ;    
		DECLARE CONTINUE HANDLER FOR NOT FOUND SET tradeNumber=null, createTime=null;
		DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET t_error=1; 
		DROP TABLE IF EXISTS notTimeOutOrder;
    CREATE TEMPORARY TABLE notTimeOutOrder(trade_number VARCHAR(28),create_time datetime,payment_time datetime,type TINYINT,STATUS TINYINT);

      
    set currentTime=NOW();
		OPEN next_cur;
    FETCH next_cur INTO tradeNumber,createTime,paymentTime,orderType,orderStatus,merchantUserId,userId,actualAmount,currencyAmount,currencyType,paymentId;    
    while tradeNumber is not null && tradeNumber!='' do    
			if (ABS(TIMESTAMPDIFF(second,createTime,currentTime))>=timeOut && orderStatus=0) || 
					( paymentTime is not null && orderStatus=1 && ABS(TIMESTAMPDIFF(second,paymentTime,currentTime))>=timeOut)
			then				

						if orderType >1 then 
								set t_error=0;
								START TRANSACTION;  

								select user_id into tempId from users where user_id=userId for update ;

								insert into journal_account(user_id,change_amount,currency_number_before,currency_number_after,freeze_number_before,freeze_number_after,create_time,remark,type,currency_type,trade_number)
								select userId,actualAmount,uw.available_credit,uw.available_credit+actualAmount,uw.freezing_quota,uw.freezing_quota-actualAmount,now(),CONCAT('订单超时回退额度：',tradeNumber),4 ,1,tradeNumber
								from users uw where uw.user_id=userId;

								select uw.wallet_id into userWalletId  from user_wallet uw where uw.user_id=o.merchant_user_id;																							

								update users uw,orders o set o.status=3,uw.`lock`=0, uw.available_credit=uw.available_credit+o.actual_amount,uw.freezing_quota=uw.freezing_quota-o.actual_amount 
								where uw.user_id=o.user_id and o.trade_number=tradeNumber;															
								
								select uw.wallet_id into hasRows from user_wallet uw where uw.wallet_id=userWalletId for update;
	
								insert into journal_account(user_id,change_amount,currency_number_before,currency_number_after,freeze_number_before,freeze_number_after,create_time,remark,type,currency_type,trade_number)
								select merchantUserId,currencyAmount,uw.currency_number,uw.currency_number+currencyAmount,uw.freeze_number,uw.freeze_number-currencyAmount,now(),CONCAT('订单超时回退币：',tradeNumber),4 ,0,tradeNumber
								from user_wallet uw where uw.wallet_id=userWalletId ;
																						
								update user_wallet uw set uw.currency_number=uw.currency_number+currencyAmount,uw.freeze_number=uw.freeze_number-currencyAmount 
								where uw.wallet_id=userWalletId;
								
								IF t_error = 1 THEN  
											 ROLLBACK;
								ELSE  
											 COMMIT;
								END IF;							
								insert into notTimeOutOrder(trade_number,create_time,payment_time,type,STATUS) values(tradeNumber,createTime,paymentTime,orderType,orderStatus);				
								
						else						
						
								select uw.wallet_id into userWalletId  from user_wallet uw where uw.user_id=userId;
								
								if userWalletId>0 then 
									set t_error=0;
									START TRANSACTION;  

									insert into journal_account(user_id,change_amount,currency_number_before,currency_number_after,freeze_number_before,freeze_number_after,create_time,remark,type,currency_type,trade_number)
									select userId,currencyAmount,uw.currency_number,uw.currency_number+currencyAmount,uw.freeze_number,uw.freeze_number-currencyAmount,now(),CONCAT('订单超时回退币：',tradeNumber),4,0,tradeNumber
									from user_wallet uw where uw.wallet_id=userWalletId;

									select uw.wallet_id into hasRows from user_wallet uw where uw.wallet_id=userWalletId for update;
									update user_wallet uw,orders o,users u set o.status=3,u.`lock`=0, uw.currency_number=uw.currency_number+currencyAmount,uw.freeze_number=uw.freeze_number-currencyAmount 
									where uw.user_id=o.user_id and u.user_id=uw.user_id and uw.wallet_id=userWalletId and o.trade_number=tradeNumber;

									select pr.payment_id into hasRows from payment_rotate pr where pr.payment_id=paymentId for update;
									update payment_rotate set payment_volume=payment_volume-actualAmount,payment_number=payment_number-1 where payment_id=paymentId;
						
									IF t_error = 1 THEN  
												 ROLLBACK;
									ELSE  
												 COMMIT;
									END IF;		

								end if ;	
							end if;
			else         				
							insert into notTimeOutOrder(trade_number , create_time,payment_time,type,STATUS) values(tradeNumber,createTime,paymentTime,orderType,orderStatus);							
			end if;					
							FETCH next_cur INTO tradeNumber,createTime,paymentTime,orderType,orderStatus,merchantUserId,userId,actualAmount,currencyAmount,currencyType,paymentId;           
    end while ;    
    close next_cur;

    select * from notTimeOutOrder;
 
		DROP TABLE IF EXISTS notTimeOutOrder;

END

END


CREATE DEFINER=`root`@`%` PROCEDURE `selectByPage`(queryStr varchar(8000),pageNumber int,pageSize int,out pageTotal int)
BEGIN

   DECLARE vstrSql VARCHAR(8000) DEFAULT '';     
    declare totalPage int;
		if instr(queryStr,'#')=0 && instr(queryStr,';')=0 then

			set vstrSql=concat('select count(*) into @countNum from(',queryStr,') as a;');    
			set @vSql=vstrSql;
			
			PREPARE stmt  FROM @vSql;    
			EXECUTE stmt ;
			DEALLOCATE PREPARE  stmt;
			
			set totalPage=TRUNCATE((@countNum+pageSize-1)/pageSize,0); 
			
			set pageTotal=@countNum;
			
			if pageNumber>0 && pageNumber<=totalPage then
					
				set vstrSql=concat(queryStr,' limit ',(pageNumber-1)*pageSize,',',pageSize);    
				
				set @resultSql=vstrSql;
				
				PREPARE stmt  FROM @resultSql;    
				EXECUTE stmt ;
				DEALLOCATE PREPARE  stmt;
			end if;
    end if;



END

CREATE DEFINER=`root`@`%` PROCEDURE `selectResourcesBypower`(userId INT)
BEGIN

   	declare powerId, roleId INT;
		
    SELECT power_id INTO powerId FROM admin_user WHERE id = userId AND is_valid = 1;
		
		SELECT role_id INTO roleId FROM power_role_detail WHERE power_id = powerId;

		SELECT r.* FROM role_resource_detail o, resources_url r WHERE o.resources_id = r.id AND o.role_id = roleId;




END

CREATE DEFINER=`root`@`%` PROCEDURE `setPaymentDefault`(paymentId LONG,userId long)
BEGIN
	declare t_error INTEGER DEFAULT 0;  	
	declare type int ;
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET t_error=1; 

 START TRANSACTION;  

	select  payment_type into type from 	payment_mode where payment_id=paymentId;

	update payment_mode set payment_default=0 where user_id=userId and payment_type=type and payment_id!=paymentId;
	
	update payment_mode set payment_default=1 where  payment_id=paymentId;

	IF t_error = 1 THEN
             ROLLBACK;
             select -1;

	ELSE  
             COMMIT;
             select 1;
	END IF;

END



CREATE DEFINER=`root`@`%` PROCEDURE `timeoutOrder`(orderIds varchar(8000))
BEGIN

  declare curIndex ,successNum ,t_error,hasRows INT DEFAULT 0;
	declare separators varchar(2) default ',';
  declare curId,tradeNumber,currencyType varchar(28);
	declare userWalletId,userId,tempId,merchantUserId,paymentId bigint; 	
	declare ordersStatus,tradeType TINYINT; 	
	declare actualAmount, currencyAmount decimal(22,8);
	declare next_cur CURSOR FOR SELECT id FROM orderId ;    
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET t_error=1; 
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET tradeNumber=null;
	DROP TABLE IF EXISTS orderId;    
	DROP TABLE IF EXISTS userWalletId;    
	CREATE TEMPORARY TABLE orderId(id varchar(28));
	CREATE TEMPORARY TABLE userWalletId(id bigint);
  set curIndex=INSTR(orderIds,separators);   
	while curIndex>0 do
		set curId=left(orderIds, curIndex-1);
		insert into orderId(id) values(curId);        
		set orderIds=substring(orderIds,curIndex+1,length(orderIds)-curIndex);        
		set curIndex=INSTR(orderIds,separators);				
	end while;
  insert into orderId(id) values(orderIds);

		OPEN next_cur;
    FETCH next_cur INTO tradeNumber ; 
		while tradeNumber is not null && tradeNumber!='' do    
			
						select type,currency_type,user_id,merchant_user_id,status,actual_amount,currency_amount,payment_id into tradeType,currencyType,userId,merchantUserId,ordersStatus,actualAmount,currencyAmount,paymentId from orders o where o.trade_number=tradeNumber and o.status<2;

						if ordersStatus=0 || ordersStatus=1 then 
							if tradeType >1 then 
									set t_error=0;
									START TRANSACTION;  

									insert into journal_account(user_id,change_amount,currency_number_before,currency_number_after,freeze_number_before,freeze_number_after,create_time,remark,type,currency_type,trade_number)
									select userId,actualAmount,uw.available_credit,uw.available_credit+actualAmount,uw.freezing_quota,uw.freezing_quota-actualAmount,now(),CONCAT('订单超时回退额度：',tradeNumber),4 ,1,tradeNumber
									from users uw where uw.user_id=userId ;

									select uw.wallet_id into userWalletId  from user_wallet uw where uw.user_id=merchantUserId and uw.currency_type=currencyType;																

									select user_id into tempId from users where user_id=userId for update ;
									update users uw,orders o set o.status=3,uw.`lock`=0, uw.available_credit=uw.available_credit+o.actual_amount,uw.freezing_quota=uw.freezing_quota-o.actual_amount 
									where uw.user_id=o.user_id and o.trade_number=tradeNumber;
																
									insert into journal_account(user_id,change_amount,currency_number_before,currency_number_after,freeze_number_before,freeze_number_after,create_time,remark,type,currency_type,trade_number)
									select merchantUserId,currencyAmount,uw.currency_number,uw.currency_number+currencyAmount,uw.freeze_number,uw.freeze_number-currencyAmount,now(),CONCAT('订单超时回退币：',tradeNumber),4 ,0,tradeNumber
									from user_wallet uw where uw.wallet_id=userWalletId;
															
									select uw.wallet_id into hasRows from user_wallet uw where uw.wallet_id=userWalletId for update;

									update user_wallet uw set uw.currency_number=uw.currency_number+currencyAmount,uw.freeze_number=uw.freeze_number-currencyAmount
									where  uw.wallet_id=userWalletId ;
																		
									IF t_error = 1 THEN  
												 ROLLBACK;
									ELSE  
												 set successNum=successNum+1;
												 COMMIT;
									END IF;											
									
							else

								select uw.wallet_id into userWalletId  from user_wallet uw where uw.user_id=userId ;

								if userWalletId>0 then 
									set t_error=0;
									START TRANSACTION;  

									insert into journal_account(user_id,change_amount,currency_number_before,currency_number_after,freeze_number_before,freeze_number_after,create_time,remark,type,currency_type,trade_number)
									select userId,currencyAmount,uw.currency_number,uw.currency_number+currencyAmount,uw.freeze_number,uw.freeze_number-currencyAmount,now(),CONCAT('订单超时回退币：',tradeNumber),4 ,0,tradeNumber
									from user_wallet uw where uw.wallet_id=userWalletId ;

									select uw.wallet_id into hasRows from user_wallet uw where uw.wallet_id=userWalletId for update;
									update user_wallet uw,orders o,users u set o.status=3,u.`lock`=0, uw.currency_number=uw.currency_number+currencyAmount,uw.freeze_number=uw.freeze_number-currencyAmount 
									where uw.user_id=o.user_id and u.user_id=uw.user_id and uw.wallet_id=userWalletId and o.trade_number=tradeNumber;

									select pr.payment_id into hasRows from payment_rotate pr where pr.payment_id=paymentId for update;
									update payment_rotate set payment_volume=payment_volume-actualAmount,payment_number=payment_number-1 where payment_id=paymentId;
										
									IF t_error = 1 THEN  
												 ROLLBACK;
									ELSE  
												 set successNum=successNum+1;
												 COMMIT;
									END IF;		

								end if ;		
							end if;
					end if;

					FETCH next_cur INTO tradeNumber;            
    end while ;    
    close next_cur;

	select successNum;
        
	truncate orderId;
	truncate userWalletId;

	DROP TABLE IF EXISTS orderId;  
	DROP TABLE IF EXISTS userWalletId; 

END

END


CREATE DEFINER=`root`@`%` PROCEDURE `updateByRoleId`(roleId INT,resources_id VARCHAR(100))
BEGIN
			declare count ,c,_id,m_id,curIndex INT;
			declare curId VARCHAR(20);
			
			DROP TABLE IF EXISTS menu_role_Id;
			CREATE TEMPORARY TABLE menu_role_Id(id varchar(30));
     
  		set curIndex=INSTR(resources_id,',');    
			while curIndex>0 do
				set curId=left(resources_id, curIndex-1);
				insert into menu_role_Id(id) values(curId);        
				set resources_id=substring(resources_id,curIndex+1,length(resources_id)-curIndex);        
				set curIndex=INSTR(resources_id,',');				
			end while;

    	insert into menu_role_Id(id) values(resources_id);
			
			delete from role_resource_detail where role_id = roleId;
			
			insert into role_resource_detail (role_id,resources_id,create_time) select roleId,id,now() from menu_role_Id;
			
			DROP TABLE IF EXISTS menu_role_Id;
    END