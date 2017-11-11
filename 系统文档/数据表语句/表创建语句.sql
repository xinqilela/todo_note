CREATE TABLE `goal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `content` varchar(45) DEFAULT NULL,
  `selfId` varchar(45) DEFAULT NULL,
  `userId` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `isdelete` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `content` varchar(45) DEFAULT NULL,
  `selfId` varchar(45) DEFAULT NULL,
  `userId` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `isdelete` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

CREATE TABLE `selftask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `content` varchar(45) DEFAULT NULL,
  `starttime` varchar(45) DEFAULT NULL,
  `endtime` varchar(45) DEFAULT NULL,
  `clocktime` varchar(45) DEFAULT NULL,
  `projectId` varchar(45) DEFAULT NULL,
  `goalId` varchar(45) DEFAULT NULL,
  `sightId` varchar(45) DEFAULT NULL,
  `userId` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `isdelete` varchar(45) DEFAULT NULL,
  `istmp` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;

CREATE TABLE `sight` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `content` varchar(45) DEFAULT NULL,
  `selfId` varchar(45) DEFAULT NULL,
  `userId` varchar(45) DEFAULT NULL,
  `isdelete` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

CREATE TABLE `team` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `teamname` varchar(45) DEFAULT NULL,
  `content` varchar(45) DEFAULT NULL,
  `leaderId` varchar(45) DEFAULT NULL,
  `teamId` varchar(45) DEFAULT NULL,
  `isdelete` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `teamjoininfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fromUserName` varchar(45) DEFAULT NULL,
  `toUserName` varchar(45) DEFAULT NULL,
  `teamId` varchar(45) DEFAULT NULL,
  `isdelete` varchar(45) DEFAULT NULL,
  `execType` varchar(45) DEFAULT NULL,
  `teamName` varchar(45) DEFAULT NULL,
  `content` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `teamtask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `content` varchar(45) DEFAULT NULL,
  `starttime` varchar(45) DEFAULT NULL,
  `endtime` varchar(45) DEFAULT NULL,
  `clocktime` varchar(45) DEFAULT NULL,
  `projectId` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `isdelete` varchar(45) DEFAULT NULL,
  `teamId` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phonenumber` varchar(45) DEFAULT NULL,
  `userId` varchar(45) DEFAULT NULL,
  `usergroup` varchar(45) DEFAULT NULL,
  `imgpath` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

CREATE TABLE `userteam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` varchar(45) DEFAULT NULL,
  `teamId` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

