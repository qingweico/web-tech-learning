import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {fetchUserData} from './actions';
import PropTypes from "prop-types";

const UserInfo = ({userId}) => {
    const dispatch = useDispatch();
    const user = useSelector((state) => state.users.user);
    const status = useSelector((state) => state.users.status);
    const error = useSelector((state) => state.users.error);

    useEffect(() => {
        dispatch(fetchUserData(userId));
    }, [dispatch, userId]);

    return (<div>
        {status === 'loading' && <p>Loading...</p>}
        {status === 'succeeded' && (<div>
            <h2>{user.name}</h2>
            <p>{user.email}</p>
        </div>)}
        {status === 'failed' && <p style={{color: "red"}}>Error: {error}</p>}
    </div>)
        ;
};
UserInfo.propTypes = {
    userId: PropTypes.number.isRequired,
};
export default UserInfo;
